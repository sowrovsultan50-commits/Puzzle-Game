package com.example.domain.engine

import com.example.data.model.Difficulty
import com.example.data.model.MatchScoreBreakdown

object ScoringEngine {

    const val BASE_SCORE = 1000
    const val WRONG_MOVE_PENALTY = 25
    const val HINT_PENALTY = 75
    const val MAX_POSSIBLE_SCORE = 5000
    const val MIN_POSSIBLE_SCORE = 100

    fun calculateScore(
        difficulty: Difficulty,
        timeTakenSeconds: Float,
        totalPieces: Int,
        wrongMoves: Int,
        hintsUsed: Int,
        rankPosition: Int = 1
    ): MatchScoreBreakdown {
        val totalAllowedTime = difficulty.timeSeconds.toFloat()
        val timeRatio = ((totalAllowedTime - timeTakenSeconds) / totalAllowedTime).coerceIn(0f, 1f)

        // Speed Bonus: up to 500 points
        val speedBonus = (timeRatio * 500f).toInt()

        // Difficulty Bonus: multiplier based on grid size
        val difficultyBonus = ((difficulty.multiplier - 1.0f) * 400f).toInt()

        // Accuracy Bonus: if zero or few wrong moves
        val accuracyRatio = (totalPieces.toFloat() / (totalPieces + wrongMoves)).coerceIn(0f, 1f)
        val accuracyBonus = (accuracyRatio * 300f).toInt()

        // Penalties
        val wrongPenalty = wrongMoves * WRONG_MOVE_PENALTY
        val hintPenalty = hintsUsed * HINT_PENALTY
        val totalPenalties = wrongPenalty + hintPenalty

        // Placement bonus for multiplayer winners
        val placementBonus = when (rankPosition) {
            1 -> 250
            2 -> 150
            3 -> 75
            else -> 0
        }

        val rawScore = BASE_SCORE + speedBonus + difficultyBonus + accuracyBonus + placementBonus - totalPenalties
        val finalScore = rawScore.coerceIn(MIN_POSSIBLE_SCORE, MAX_POSSIBLE_SCORE)

        // Rewards calculation
        val coins = (finalScore / 20) + (if (rankPosition == 1) 50 else 20)
        val xp = (finalScore / 10) + (if (rankPosition == 1) 100 else 40)

        return MatchScoreBreakdown(
            baseScore = BASE_SCORE,
            speedBonus = speedBonus,
            accuracyBonus = accuracyBonus,
            difficultyBonus = difficultyBonus,
            wrongPenalty = wrongPenalty,
            hintPenalty = hintPenalty,
            finalScore = finalScore,
            timeTakenSeconds = timeTakenSeconds,
            totalPieces = totalPieces,
            wrongMoves = wrongMoves,
            hintsUsed = hintsUsed,
            rankPosition = rankPosition,
            coinsEarned = coins,
            xpEarned = xp
        )
    }
}
