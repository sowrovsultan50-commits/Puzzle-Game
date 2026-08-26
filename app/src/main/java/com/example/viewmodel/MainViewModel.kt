package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ads.AdManager
import com.example.data.local.CompletedPuzzleEntity
import com.example.data.local.GameRepository
import com.example.data.local.UserStatsEntity
import com.example.data.model.Difficulty
import com.example.data.model.PlayerInfo
import com.example.data.model.PuzzleCategory
import com.example.data.model.PuzzleItem
import com.example.data.model.RankTier
import com.example.data.model.RoomInfo
import com.example.data.model.ShopItem
import com.example.data.repository.GameExtrasCatalog
import com.example.data.repository.LeaderboardCatalog
import com.example.data.repository.LeaderboardEntry
import com.example.data.repository.LeaderboardType
import com.example.data.repository.PuzzleCatalog
import com.example.domain.audio.SoundManager
import com.example.firebase.AuthManager
import com.example.firebase.MultiplayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = GameRepository.getInstance(application)
    val soundManager = SoundManager.getInstance(application)
    val multiplayerService = MultiplayerService.getInstance()
    val authManager = AuthManager.getInstance(application, repository)
    val adManager = AdManager.getInstance(application)

    val userStats: StateFlow<UserStatsEntity> = repository.userStats
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserStatsEntity())

    val completedPuzzles: StateFlow<List<CompletedPuzzleEntity>> = repository.completedPuzzles
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val currentRoom: StateFlow<RoomInfo?> = multiplayerService.currentRoom
    val isConnecting: StateFlow<Boolean> = multiplayerService.isConnecting
    val connectionError: StateFlow<String?> = multiplayerService.connectionError

    private val _selectedCategory = MutableStateFlow<PuzzleCategory>(PuzzleCatalog.categories.first())
    val selectedCategory: StateFlow<PuzzleCategory> = _selectedCategory.asStateFlow()

    private val _selectedPuzzle = MutableStateFlow<PuzzleItem>(PuzzleCatalog.puzzles.first())
    val selectedPuzzle: StateFlow<PuzzleItem> = _selectedPuzzle.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow<Difficulty>(Difficulty.EASY_3X3)
    val selectedDifficulty: StateFlow<Difficulty> = _selectedDifficulty.asStateFlow()

    private val _isKidsMode = MutableStateFlow(false)
    val isKidsMode: StateFlow<Boolean> = _isKidsMode.asStateFlow()

    private val _leaderboardType = MutableStateFlow(LeaderboardType.GLOBAL)
    val leaderboardType: StateFlow<LeaderboardType> = _leaderboardType.asStateFlow()

    private val _dailyCompletedToday = MutableStateFlow(false)
    val dailyCompletedToday: StateFlow<Boolean> = _dailyCompletedToday.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getInitialUserStats()
            checkDailyChallenge()
        }
    }

    fun selectCategory(category: PuzzleCategory) {
        _selectedCategory.value = category
        soundManager.playButtonClick()
    }

    fun selectPuzzle(puzzle: PuzzleItem) {
        _selectedPuzzle.value = puzzle
        soundManager.playButtonClick()
    }

    fun selectDifficulty(difficulty: Difficulty) {
        _selectedDifficulty.value = difficulty
        soundManager.playButtonClick()
    }

    fun setKidsMode(isKids: Boolean) {
        _isKidsMode.value = isKids
        viewModelScope.launch {
            val stats = userStats.value
            repository.updateSettings(
                sound = stats.soundEnabled,
                music = stats.musicEnabled,
                haptics = stats.hapticEnabled,
                kidsMode = isKids
            )
        }
    }

    fun toggleSound(enabled: Boolean) {
        soundManager.isSoundEnabled = enabled
        viewModelScope.launch {
            val stats = userStats.value
            repository.updateSettings(
                sound = enabled,
                music = stats.musicEnabled,
                haptics = stats.hapticEnabled,
                kidsMode = stats.isKidsMode
            )
        }
    }

    fun toggleMusic(enabled: Boolean) {
        soundManager.isMusicEnabled = enabled
        viewModelScope.launch {
            val stats = userStats.value
            repository.updateSettings(
                sound = stats.soundEnabled,
                music = enabled,
                haptics = stats.hapticEnabled,
                kidsMode = stats.isKidsMode
            )
        }
    }

    fun toggleHaptic(enabled: Boolean) {
        soundManager.isHapticEnabled = enabled
        viewModelScope.launch {
            val stats = userStats.value
            repository.updateSettings(
                sound = stats.soundEnabled,
                music = stats.musicEnabled,
                haptics = enabled,
                kidsMode = stats.isKidsMode
            )
        }
    }

    fun updateProfile(name: String, avatarId: String) {
        viewModelScope.launch {
            repository.updateUserProfile(name, avatarId)
            soundManager.playButtonClick()
        }
    }

    fun setParentalLock(locked: Boolean, pin: String) {
        viewModelScope.launch {
            repository.setParentalControl(locked, pin)
            soundManager.playButtonClick()
        }
    }

    fun buyShopItem(item: ShopItem, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.spendCoins(item.priceCoins)
            if (success) {
                soundManager.playAchievementUnlocked()
                if (item.assetRef.startsWith("avatar_")) {
                    repository.updateUserProfile(userStats.value.username, item.assetRef)
                }
            }
            onComplete(success)
        }
    }

    fun watchRewardedAdForCoins() {
        viewModelScope.launch {
            adManager.showRewardedAd { _, amount ->
                viewModelScope.launch {
                    repository.addCoins(amount)
                    soundManager.playAchievementUnlocked()
                }
            }
        }
    }

    fun setLeaderboardType(type: LeaderboardType) {
        _leaderboardType.value = type
        soundManager.playButtonClick()
    }

    fun getLeaderboardEntries(): List<LeaderboardEntry> {
        val stats = userStats.value
        return LeaderboardCatalog.getLeaderboard(_leaderboardType.value, stats.bestScore, stats.username)
    }

    fun startQuickMatch(playerCount: Int) {
        val stats = userStats.value
        val player = PlayerInfo(
            userId = stats.userId,
            name = stats.username,
            avatarId = stats.avatarId,
            rank = RankTier.fromXp(stats.xp)
        )
        multiplayerService.startQuickMatch(
            player = player,
            targetPlayerCount = playerCount,
            difficulty = _selectedDifficulty.value,
            puzzleId = _selectedPuzzle.value.id
        )
    }

    fun createPrivateRoom(): String {
        val stats = userStats.value
        val player = PlayerInfo(
            userId = stats.userId,
            name = stats.username,
            avatarId = stats.avatarId,
            rank = RankTier.fromXp(stats.xp)
        )
        return multiplayerService.createPrivateRoom(
            hostPlayer = player,
            puzzleId = _selectedPuzzle.value.id,
            difficulty = _selectedDifficulty.value
        )
    }

    fun joinPrivateRoom(code: String, onResult: (Boolean, String?) -> Unit) {
        val stats = userStats.value
        val player = PlayerInfo(
            userId = stats.userId,
            name = stats.username,
            avatarId = stats.avatarId,
            rank = RankTier.fromXp(stats.xp)
        )
        multiplayerService.joinPrivateRoom(code, player, onResult)
    }

    fun togglePlayerReady() {
        multiplayerService.togglePlayerReady(userStats.value.userId)
        soundManager.playButtonClick()
    }

    fun startCountdown() {
        soundManager.playCountdownGo()
        multiplayerService.startMatchCountdown()
    }

    fun leaveRoom() {
        multiplayerService.leaveRoom(userStats.value.userId)
    }

    private suspend fun checkDailyChallenge() {
        _dailyCompletedToday.value = repository.hasCompletedTodayDailyChallenge()
    }
}
