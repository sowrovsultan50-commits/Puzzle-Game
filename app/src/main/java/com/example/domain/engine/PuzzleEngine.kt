package com.example.domain.engine

import com.example.data.model.Difficulty
import com.example.data.model.PuzzlePiece
import kotlin.random.Random

class PuzzleEngine(
    val difficulty: Difficulty,
    val puzzleSeed: Long = System.currentTimeMillis()
) {
    val totalPieces = difficulty.rows * difficulty.cols

    private val _originalPieces = mutableListOf<PuzzlePiece>()
    val originalPieces: List<PuzzlePiece> get() = _originalPieces

    private val _trayPieces = mutableListOf<PuzzlePiece>()
    val trayPieces: List<PuzzlePiece> get() = _trayPieces

    // Board grid holds placed piece for each cell [0 until totalPieces]
    private val _boardGrid = Array<PuzzlePiece?>(totalPieces) { null }
    val boardGrid: Array<PuzzlePiece?> get() = _boardGrid

    var wrongMovesCount: Int = 0
        private set

    var hintsUsedCount: Int = 0
        private set

    var isGhostVisible: Boolean = false

    var highlightedTargetSlot: Int? = null

    init {
        initializePieces()
    }

    private fun initializePieces() {
        _originalPieces.clear()
        _trayPieces.clear()
        for (i in 0 until totalPieces) {
            _boardGrid[i] = null
        }

        var index = 0
        for (r in 0 until difficulty.rows) {
            for (c in 0 until difficulty.cols) {
                val piece = PuzzlePiece(
                    id = index,
                    originalIndex = index,
                    originalRow = r,
                    originalCol = c,
                    currentIndex = index,
                    isPlacedCorrectly = false,
                    isInTray = true
                )
                _originalPieces.add(piece)
                index++
            }
        }

        // Deterministic shuffle with seed
        val rng = Random(puzzleSeed)
        val shuffled = _originalPieces.map { it.copy() }.shuffled(rng)
        _trayPieces.addAll(shuffled)
    }

    fun getProgressPercent(): Int {
        val correctlyPlaced = _boardGrid.count { it != null && it.isPlacedCorrectly }
        return ((correctlyPlaced.toFloat() / totalPieces) * 100).toInt()
    }

    fun isCompleted(): Boolean {
        return _boardGrid.all { it != null && it.isPlacedCorrectly }
    }

    fun placePieceAtSlot(pieceId: Int, targetSlotIndex: Int): Boolean {
        highlightedTargetSlot = null
        val piece = _originalPieces.find { it.id == pieceId } ?: return false

        // Verify if targetSlotIndex matches piece's originalIndex
        if (targetSlotIndex == piece.originalIndex) {
            // Correct placement
            val updatedPiece = piece.copy(
                currentIndex = targetSlotIndex,
                isPlacedCorrectly = true,
                isInTray = false
            )
            _boardGrid[targetSlotIndex] = updatedPiece
            _trayPieces.removeAll { it.id == pieceId }
            return true
        } else {
            // Wrong placement
            wrongMovesCount++
            return false
        }
    }

    fun useHighlightHint(selectedPieceId: Int? = null): Int? {
        hintsUsedCount++
        val pieceToHint = if (selectedPieceId != null) {
            _originalPieces.find { it.id == selectedPieceId }
        } else {
            _trayPieces.firstOrNull()
        }

        return pieceToHint?.originalIndex?.also {
            highlightedTargetSlot = it
        }
    }

    fun toggleGhostHint(): Boolean {
        hintsUsedCount++
        isGhostVisible = !isGhostVisible
        return isGhostVisible
    }

    fun autoPlaceOnePieceHint(): PuzzlePiece? {
        hintsUsedCount++
        val nextPiece = _trayPieces.firstOrNull() ?: return null
        val targetSlot = nextPiece.originalIndex
        val placed = nextPiece.copy(
            currentIndex = targetSlot,
            isPlacedCorrectly = true,
            isInTray = false
        )
        _boardGrid[targetSlot] = placed
        _trayPieces.removeAll { it.id == nextPiece.id }
        highlightedTargetSlot = null
        return placed
    }
}
