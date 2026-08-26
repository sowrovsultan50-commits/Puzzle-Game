package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.GameRepository
import com.example.data.model.Difficulty
import com.example.data.model.MatchScoreBreakdown
import com.example.data.model.PlayerInfo
import com.example.data.model.PuzzleItem
import com.example.data.model.RoomInfo
import com.example.data.model.RoomState
import com.example.data.repository.PuzzleCatalog
import com.example.domain.audio.SoundManager
import com.example.domain.engine.AntiCheatValidator
import com.example.domain.engine.PuzzleEngine
import com.example.domain.engine.ScoringEngine
import com.example.firebase.MultiplayerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepository.getInstance(application)
    private val soundManager = SoundManager.getInstance(application)
    private val multiplayerService = MultiplayerService.getInstance()

    private val _currentPuzzle = MutableStateFlow<PuzzleItem>(PuzzleCatalog.puzzles.first())
    val currentPuzzle: StateFlow<PuzzleItem> = _currentPuzzle.asStateFlow()

    private val _difficulty = MutableStateFlow(Difficulty.EASY_3X3)
    val difficulty: StateFlow<Difficulty> = _difficulty.asStateFlow()

    private val _puzzleEngine = MutableStateFlow<PuzzleEngine?>(null)
    val puzzleEngine: StateFlow<PuzzleEngine?> = _puzzleEngine.asStateFlow()

    private val _timeRemainingSeconds = MutableStateFlow(120)
    val timeRemainingSeconds: StateFlow<Int> = _timeRemainingSeconds.asStateFlow()

    private val _isGameActive = MutableStateFlow(false)
    val isGameActive: StateFlow<Boolean> = _isGameActive.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _isMultiplayer = MutableStateFlow(false)
    val isMultiplayer: StateFlow<Boolean> = _isMultiplayer.asStateFlow()

    private val _matchResult = MutableStateFlow<MatchScoreBreakdown?>(null)
    val matchResult: StateFlow<MatchScoreBreakdown?> = _matchResult.asStateFlow()

    private val _showHintDialog = MutableStateFlow(false)
    val showHintDialog: StateFlow<Boolean> = _showHintDialog.asStateFlow()

    val currentRoom: StateFlow<RoomInfo?> = multiplayerService.currentRoom

    private var timerJob: Job? = null
    private var startTimeMs: Long = 0L

    fun startPracticeMatch(puzzleItem: PuzzleItem, diff: Difficulty) {
        _isMultiplayer.value = false
        _currentPuzzle.value = puzzleItem
        _difficulty.value = diff
        _matchResult.value = null
        _isPaused.value = false

        val engine = PuzzleEngine(difficulty = diff, puzzleSeed = System.currentTimeMillis())
        _puzzleEngine.value = engine
        _timeRemainingSeconds.value = diff.timeSeconds
        _isGameActive.value = true
        startTimeMs = System.currentTimeMillis()

        startTimer()
    }

    fun startMultiplayerMatch(room: RoomInfo) {
        _isMultiplayer.value = true
        val puzzle = PuzzleCatalog.getPuzzleById(room.puzzleId)
        _currentPuzzle.value = puzzle
        _difficulty.value = room.difficulty
        _matchResult.value = null
        _isPaused.value = false

        val engine = PuzzleEngine(difficulty = room.difficulty, puzzleSeed = room.puzzleSeed)
        _puzzleEngine.value = engine
        _timeRemainingSeconds.value = room.difficulty.timeSeconds
        _isGameActive.value = true
        startTimeMs = System.currentTimeMillis()

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeRemainingSeconds.value > 0 && _isGameActive.value) {
                delay(1000)
                if (!_isPaused.value) {
                    _timeRemainingSeconds.value -= 1
                    if (_timeRemainingSeconds.value in 1..5) {
                        soundManager.playCountdownTick()
                    }
                }
            }

            if (_timeRemainingSeconds.value <= 0 && _isGameActive.value) {
                soundManager.playDefeat()
                finishGame(isTimeout = true)
            }
        }
    }

    fun handlePiecePlacement(pieceId: Int, targetSlot: Int) {
        val engine = _puzzleEngine.value ?: return
        if (!_isGameActive.value || _isPaused.value) return

        val isSuccess = engine.placePieceAtSlot(pieceId, targetSlot)
        if (isSuccess) {
            soundManager.playPieceSnap()

            val progress = engine.getProgressPercent()
            if (_isMultiplayer.value) {
                val stats = repository.userStats
                viewModelScope.launch {
                    val userStats = repository.getInitialUserStats()
                    multiplayerService.updateMyProgress(
                        userId = userStats.userId,
                        progressPercent = progress,
                        score = 0,
                        isFinished = false,
                        finishTimeSeconds = 0f
                    )
                }
            }

            if (engine.isCompleted()) {
                soundManager.playVictory()
                finishGame(isTimeout = false)
            }
        } else {
            soundManager.playWrongMove()
        }
    }

    fun togglePause() {
        _isPaused.value = !_isPaused.value
    }

    fun openHintDialog() {
        _showHintDialog.value = true
    }

    fun closeHintDialog() {
        _showHintDialog.value = false
    }

    fun useHighlightHint() {
        val engine = _puzzleEngine.value ?: return
        viewModelScope.launch {
            if (repository.spendCoins(15)) {
                engine.useHighlightHint()
                soundManager.playButtonClick()
                closeHintDialog()
            }
        }
    }

    fun useGhostHint() {
        val engine = _puzzleEngine.value ?: return
        viewModelScope.launch {
            if (repository.spendCoins(25)) {
                engine.toggleGhostHint()
                soundManager.playButtonClick()
                closeHintDialog()
            }
        }
    }

    fun useAutoPlaceHint() {
        val engine = _puzzleEngine.value ?: return
        viewModelScope.launch {
            if (repository.spendCoins(50)) {
                engine.autoPlaceOnePieceHint()
                soundManager.playPieceSnap()
                closeHintDialog()

                if (engine.isCompleted()) {
                    soundManager.playVictory()
                    finishGame(isTimeout = false)
                }
            }
        }
    }

    private fun finishGame(isTimeout: Boolean) {
        _isGameActive.value = false
        timerJob?.cancel()

        val engine = _puzzleEngine.value ?: return
        val completionTimestamp = System.currentTimeMillis()
        val elapsedSeconds = if (isTimeout) {
            _difficulty.value.timeSeconds.toFloat()
        } else {
            ((completionTimestamp - startTimeMs) / 1000f).coerceAtLeast(1f)
        }

        val result = ScoringEngine.calculateScore(
            difficulty = _difficulty.value,
            timeTakenSeconds = elapsedSeconds,
            totalPieces = engine.totalPieces,
            wrongMoves = engine.wrongMovesCount,
            hintsUsed = engine.hintsUsedCount,
            rankPosition = if (isTimeout) 4 else 1
        )

        // Anti cheat verification
        val validation = AntiCheatValidator.validateMatchResult(
            difficulty = _difficulty.value,
            timeTakenSeconds = elapsedSeconds,
            scoreResult = result,
            startTimestamp = startTimeMs,
            completionTimestamp = completionTimestamp
        )

        if (!validation.isValid) {
            // Penalize or invalidate exploit
            _matchResult.value = result.copy(finalScore = 100, xpEarned = 10, coinsEarned = 5)
        } else {
            _matchResult.value = result
        }

        viewModelScope.launch {
            val userStats = repository.getInitialUserStats()
            repository.recordMatchCompletion(
                puzzleId = _currentPuzzle.value.id,
                difficulty = _difficulty.value,
                scoreResult = _matchResult.value ?: result,
                isWin = !isTimeout,
                isMultiplayer = _isMultiplayer.value
            )

            if (_isMultiplayer.value) {
                multiplayerService.updateMyProgress(
                    userId = userStats.userId,
                    progressPercent = 100,
                    score = (_matchResult.value ?: result).finalScore,
                    isFinished = true,
                    finishTimeSeconds = elapsedSeconds
                )
            }
        }
    }

    fun restartMatch() {
        startPracticeMatch(_currentPuzzle.value, _difficulty.value)
    }

    fun requestMultiplayerRematch() {
        multiplayerService.requestRematch()
    }
}
