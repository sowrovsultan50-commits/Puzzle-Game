package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Difficulty
import com.example.domain.engine.AntiCheatValidator
import com.example.domain.engine.PuzzleEngine
import com.example.domain.engine.ScoringEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Picture Puzzle Battle", appName)
    }

    @Test
    fun `puzzle engine initializes correct piece counts and supports deterministic shuffling`() {
        val seed = 987654L
        val engine1 = PuzzleEngine(Difficulty.EASY_3X3, puzzleSeed = seed)
        val engine2 = PuzzleEngine(Difficulty.EASY_3X3, puzzleSeed = seed)

        assertEquals(9, engine1.totalPieces)
        assertEquals(9, engine1.trayPieces.size)
        // Same seed yields identical initial piece order for multiplayer synchronization
        assertEquals(engine1.trayPieces.map { it.id }, engine2.trayPieces.map { it.id })

        assertFalse(engine1.isCompleted())
        assertEquals(0, engine1.getProgressPercent())
    }

    @Test
    fun `puzzle engine piece placement and snapping logic`() {
        val engine = PuzzleEngine(Difficulty.EASY_2X2, puzzleSeed = 1234L)
        assertEquals(4, engine.totalPieces)

        // Find piece with originalIndex 0
        val piece0 = engine.originalPieces.first { it.originalIndex == 0 }

        // Wrong placement test
        val wrongResult = engine.placePieceAtSlot(piece0.id, targetSlotIndex = 1)
        assertFalse(wrongResult)
        assertEquals(1, engine.wrongMovesCount)

        // Correct placement test
        val correctResult = engine.placePieceAtSlot(piece0.id, targetSlotIndex = 0)
        assertTrue(correctResult)
        assertEquals(25, engine.getProgressPercent())
        assertEquals(3, engine.trayPieces.size)
    }

    @Test
    fun `scoring engine calculates bonuses and penalties correctly`() {
        val breakdown = ScoringEngine.calculateScore(
            difficulty = Difficulty.MEDIUM_4X4,
            timeTakenSeconds = 30f,
            totalPieces = 16,
            wrongMoves = 0,
            hintsUsed = 0,
            rankPosition = 1
        )

        assertTrue(breakdown.finalScore > 1000)
        assertEquals(0, breakdown.wrongPenalty)
        assertEquals(1, breakdown.rankPosition)
        assertTrue(breakdown.coinsEarned > 0)
        assertTrue(breakdown.xpEarned > 0)
    }

    @Test
    fun `anti cheat validator blocks impossible times`() {
        val result = ScoringEngine.calculateScore(
            difficulty = Difficulty.HARD_5X5,
            timeTakenSeconds = 0.5f,
            totalPieces = 25,
            wrongMoves = 0,
            hintsUsed = 0
        )

        val validation = AntiCheatValidator.validateMatchResult(
            difficulty = Difficulty.HARD_5X5,
            timeTakenSeconds = 0.5f,
            scoreResult = result,
            startTimestamp = 1000L,
            completionTimestamp = 1500L
        )

        assertFalse(validation.isValid)
    }
}
