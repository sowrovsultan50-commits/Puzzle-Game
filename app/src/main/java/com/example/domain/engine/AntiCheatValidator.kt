package com.example.domain.engine

import com.example.data.model.Difficulty
import com.example.data.model.MatchScoreBreakdown

object AntiCheatValidator {

    private const val MIN_SECONDS_PER_PIECE = 0.25f // Minimum humanly possible time per piece

    data class ValidationResult(
        val isValid: Boolean,
        val reason: String? = null
    )

    fun validateMatchResult(
        difficulty: Difficulty,
        timeTakenSeconds: Float,
        scoreResult: MatchScoreBreakdown,
        startTimestamp: Long,
        completionTimestamp: Long
    ): ValidationResult {
        // Check 1: Time consistency
        if (timeTakenSeconds <= 0f) {
            return ValidationResult(false, "Invalid time duration")
        }

        // Check 2: Minimum human feasible time
        val minFeasibleTime = difficulty.pieceCount * MIN_SECONDS_PER_PIECE
        if (timeTakenSeconds < minFeasibleTime) {
            return ValidationResult(false, "Completion time impossibly fast")
        }

        // Check 3: Timestamp delta verification
        val elapsedMillis = completionTimestamp - startTimestamp
        val elapsedSeconds = elapsedMillis / 1000f
        if (elapsedMillis > 0 && Math.abs(elapsedSeconds - timeTakenSeconds) > 5.0f) {
            return ValidationResult(false, "Timestamp mismatch with local timer")
        }

        // Check 4: Score bounds
        if (scoreResult.finalScore > ScoringEngine.MAX_POSSIBLE_SCORE || scoreResult.finalScore < ScoringEngine.MIN_POSSIBLE_SCORE) {
            return ValidationResult(false, "Score outside authorized parameters")
        }

        return ValidationResult(true)
    }
}
