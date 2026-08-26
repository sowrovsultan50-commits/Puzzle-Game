package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val userId: String = "local_player",
    val username: String = "PuzzleMaster",
    val avatarId: String = "avatar_1",
    val level: Int = 1,
    val xp: Int = 150,
    val coins: Int = 300,
    val totalGames: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val bestTimeSeconds: Float = 0f,
    val bestScore: Int = 0,
    val puzzlesCompletedCount: Int = 0,
    val hintsRemaining: Int = 5,
    val isKidsMode: Boolean = false,
    val isParentalLocked: Boolean = false,
    val parentPin: String = "",
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val hapticEnabled: Boolean = true
)

@Entity(tableName = "completed_puzzles")
data class CompletedPuzzleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val puzzleId: String,
    val difficulty: String,
    val score: Int,
    val timeSeconds: Float,
    val movesCount: Int,
    val hintsUsed: Int,
    val stars: Int,
    val isMultiplayer: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_challenges")
data class DailyChallengeRecordEntity(
    @PrimaryKey val dateKey: String, // e.g. "2026-08-26"
    val puzzleId: String,
    val score: Int,
    val timeSeconds: Float,
    val completedAt: Long = System.currentTimeMillis()
)
