package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    fun getUserStats(userId: String = "local_player"): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    suspend fun getUserStatsOnce(userId: String = "local_player"): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserStats(stats: UserStatsEntity)

    @Query("SELECT * FROM completed_puzzles ORDER BY timestamp DESC")
    fun getAllCompletedPuzzles(): Flow<List<CompletedPuzzleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedPuzzle(record: CompletedPuzzleEntity)

    @Query("SELECT * FROM daily_challenges WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getDailyChallengeRecord(dateKey: String): DailyChallengeRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyChallengeRecord(record: DailyChallengeRecordEntity)

    @Query("SELECT COUNT(*) FROM completed_puzzles")
    fun getCompletedCountFlow(): Flow<Int>
}
