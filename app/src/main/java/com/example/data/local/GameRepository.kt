package com.example.data.local

import android.content.Context
import com.example.data.model.Difficulty
import com.example.data.model.MatchScoreBreakdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameRepository(private val gameDao: GameDao) {

    val userStats: Flow<UserStatsEntity> = gameDao.getUserStats().map { it ?: UserStatsEntity() }
    val completedPuzzles: Flow<List<CompletedPuzzleEntity>> = gameDao.getAllCompletedPuzzles()

    suspend fun getInitialUserStats(): UserStatsEntity {
        return withContext(Dispatchers.IO) {
            val stats = gameDao.getUserStatsOnce()
            if (stats == null) {
                val newStats = UserStatsEntity()
                gameDao.insertOrUpdateUserStats(newStats)
                newStats
            } else {
                stats
            }
        }
    }

    suspend fun updateUserProfile(username: String, avatarId: String) {
        withContext(Dispatchers.IO) {
            val current = getInitialUserStats()
            val updated = current.copy(username = username, avatarId = avatarId)
            gameDao.insertOrUpdateUserStats(updated)
        }
    }

    suspend fun updateSettings(sound: Boolean, music: Boolean, haptics: Boolean, kidsMode: Boolean) {
        withContext(Dispatchers.IO) {
            val current = getInitialUserStats()
            val updated = current.copy(
                soundEnabled = sound,
                musicEnabled = music,
                hapticEnabled = haptics,
                isKidsMode = kidsMode
            )
            gameDao.insertOrUpdateUserStats(updated)
        }
    }

    suspend fun setParentalControl(locked: Boolean, pin: String) {
        withContext(Dispatchers.IO) {
            val current = getInitialUserStats()
            val updated = current.copy(isParentalLocked = locked, parentPin = pin)
            gameDao.insertOrUpdateUserStats(updated)
        }
    }

    suspend fun addCoins(amount: Int) {
        withContext(Dispatchers.IO) {
            val current = getInitialUserStats()
            val updated = current.copy(coins = (current.coins + amount).coerceAtLeast(0))
            gameDao.insertOrUpdateUserStats(updated)
        }
    }

    suspend fun spendCoins(amount: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val current = getInitialUserStats()
            if (current.coins >= amount) {
                gameDao.insertOrUpdateUserStats(current.copy(coins = current.coins - amount))
                true
            } else {
                false
            }
        }
    }

    suspend fun recordMatchCompletion(
        puzzleId: String,
        difficulty: Difficulty,
        scoreResult: MatchScoreBreakdown,
        isWin: Boolean,
        isMultiplayer: Boolean
    ) {
        withContext(Dispatchers.IO) {
            val current = getInitialUserStats()

            val newXp = current.xp + scoreResult.xpEarned
            val newLevel = 1 + (newXp / 500)
            val newCoins = current.coins + scoreResult.coinsEarned
            val newTotalGames = current.totalGames + 1
            val newWins = if (isWin) current.wins + 1 else current.wins
            val newLosses = if (!isWin && isMultiplayer) current.losses + 1 else current.losses
            val newBestScore = maxOf(current.bestScore, scoreResult.finalScore)
            val newBestTime = if (current.bestTimeSeconds <= 0f) {
                scoreResult.timeTakenSeconds
            } else {
                minOf(current.bestTimeSeconds, scoreResult.timeTakenSeconds)
            }

            val updatedStats = current.copy(
                level = newLevel,
                xp = newXp,
                coins = newCoins,
                totalGames = newTotalGames,
                wins = newWins,
                losses = newLosses,
                bestScore = newBestScore,
                bestTimeSeconds = newBestTime,
                puzzlesCompletedCount = current.puzzlesCompletedCount + 1
            )
            gameDao.insertOrUpdateUserStats(updatedStats)

            val stars = when {
                scoreResult.finalScore >= 1400 -> 3
                scoreResult.finalScore >= 900 -> 2
                else -> 1
            }

            val historyRecord = CompletedPuzzleEntity(
                puzzleId = puzzleId,
                difficulty = difficulty.title,
                score = scoreResult.finalScore,
                timeSeconds = scoreResult.timeTakenSeconds,
                movesCount = scoreResult.wrongMoves + scoreResult.totalPieces,
                hintsUsed = scoreResult.hintsUsed,
                stars = stars,
                isMultiplayer = isMultiplayer
            )
            gameDao.insertCompletedPuzzle(historyRecord)
        }
    }

    suspend fun getTodayDailyChallengeKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    suspend fun hasCompletedTodayDailyChallenge(): Boolean {
        return withContext(Dispatchers.IO) {
            val key = getTodayDailyChallengeKey()
            gameDao.getDailyChallengeRecord(key) != null
        }
    }

    suspend fun recordDailyChallenge(puzzleId: String, score: Int, timeSeconds: Float) {
        withContext(Dispatchers.IO) {
            val key = getTodayDailyChallengeKey()
            gameDao.insertDailyChallengeRecord(
                DailyChallengeRecordEntity(
                    dateKey = key,
                    puzzleId = puzzleId,
                    score = score,
                    timeSeconds = timeSeconds
                )
            )
            addCoins(200) // Bonus for daily challenge
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GameRepository? = null

        fun getInstance(context: Context): GameRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val instance = GameRepository(db.gameDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
