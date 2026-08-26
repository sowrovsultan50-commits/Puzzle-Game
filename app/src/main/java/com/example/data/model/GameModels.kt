package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class Difficulty(
    val title: String,
    val rows: Int,
    val cols: Int,
    val timeSeconds: Int,
    val multiplier: Float,
    val pieceCount: Int,
    val isKidsFriendly: Boolean = true
) {
    EASY_2X2("Easy 2×2", 2, 2, 90, 1.0f, 4, true),
    EASY_3X3("Easy 3×3", 3, 3, 120, 1.2f, 9, true),
    MEDIUM_4X4("Medium 4×4", 4, 4, 150, 1.5f, 16, true),
    HARD_5X5("Hard 5×5", 5, 5, 180, 2.0f, 25, false),
    HARD_6X6("Hard 6×6", 6, 6, 210, 2.5f, 36, false),
    EXPERT_7X7("Expert 7×7", 7, 7, 240, 3.0f, 49, false),
    EXPERT_8X8("Expert 8×8", 8, 8, 300, 3.5f, 64, false);

    companion object {
        fun fromPieceCount(count: Int): Difficulty {
            return entries.firstOrNull { it.pieceCount == count } ?: EASY_3X3
        }
    }
}

enum class CategoryGroup(val label: String) {
    KIDS("Kids Friendly"),
    GENERAL("General & Pro")
}

data class PuzzleCategory(
    val id: String,
    val name: String,
    val group: CategoryGroup,
    val iconName: String,
    val description: String
)

data class PuzzleItem(
    val id: String,
    val title: String,
    val categoryId: String,
    @DrawableRes val drawableRes: Int,
    val isKids: Boolean,
    val defaultDifficulty: Difficulty = Difficulty.EASY_3X3,
    val tags: List<String> = emptyList()
)

data class PuzzlePiece(
    val id: Int,
    val originalIndex: Int,
    val originalRow: Int,
    val originalCol: Int,
    var currentIndex: Int,
    var isPlacedCorrectly: Boolean = false,
    var isInTray: Boolean = true
)

enum class RoomState {
    WAITING,
    READY,
    STARTING,
    PLAYING,
    FINISHED,
    CANCELLED
}

enum class RankTier(val title: String, val minXp: Int, val colorHex: Long) {
    BRONZE("Bronze", 0, 0xFFCD7F32),
    SILVER("Silver", 500, 0xFFC0C0C0),
    GOLD("Gold", 1500, 0xFFFFD700),
    PLATINUM("Platinum", 3500, 0xFF00E5FF),
    DIAMOND("Diamond", 7500, 0xFFB388FF);

    companion object {
        fun fromXp(xp: Int): RankTier {
            return entries.lastOrNull { xp >= it.minXp } ?: BRONZE
        }
    }
}

data class PlayerInfo(
    val userId: String = "",
    val name: String = "Player",
    val avatarId: String = "avatar_1",
    val isHost: Boolean = false,
    val isReady: Boolean = false,
    val progressPercent: Int = 0,
    val score: Int = 0,
    val finishTimeSeconds: Float = 0f,
    val isConnected: Boolean = true,
    val rank: RankTier = RankTier.BRONZE,
    val isFinished: Boolean = false
)

data class RoomInfo(
    val roomId: String = "",
    val hostId: String = "",
    val maxPlayers: Int = 4,
    val state: RoomState = RoomState.WAITING,
    val puzzleId: String = "safari_animals",
    val difficulty: Difficulty = Difficulty.EASY_3X3,
    val puzzleSeed: Long = 12345L,
    val startTimestamp: Long = 0L,
    val players: Map<String, PlayerInfo> = emptyMap(),
    val winnerId: String? = null
)

data class MatchScoreBreakdown(
    val baseScore: Int = 1000,
    val speedBonus: Int = 0,
    val accuracyBonus: Int = 0,
    val difficultyBonus: Int = 0,
    val wrongPenalty: Int = 0,
    val hintPenalty: Int = 0,
    val finalScore: Int = 0,
    val timeTakenSeconds: Float = 0f,
    val totalPieces: Int = 9,
    val wrongMoves: Int = 0,
    val hintsUsed: Int = 0,
    val rankPosition: Int = 1,
    val coinsEarned: Int = 50,
    val xpEarned: Int = 120,
    val isNewBest: Boolean = false
)

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val requiredCount: Int,
    val currentCount: Int = 0,
    val isUnlocked: Boolean = false,
    val coinReward: Int = 100
)

data class ShopItem(
    val id: String,
    val title: String,
    val type: ShopItemType,
    val priceCoins: Int,
    val isPurchased: Boolean = false,
    val assetRef: String = ""
)

enum class ShopItemType {
    AVATAR,
    THEME,
    HINT_PACK,
    PUZZLE_PACK
}
