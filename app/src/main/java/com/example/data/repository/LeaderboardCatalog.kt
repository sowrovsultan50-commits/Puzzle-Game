package com.example.data.repository

import com.example.data.model.RankTier

data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarId: String,
    val score: Int,
    val wins: Int,
    val tier: RankTier,
    val isCurrentUser: Boolean = false
)

enum class LeaderboardType(val title: String) {
    GLOBAL("Global"),
    WEEKLY("This Week"),
    DAILY("Today's Challenge"),
    FRIENDS("Friends")
}

object LeaderboardCatalog {

    fun getLeaderboard(type: LeaderboardType, currentUserScore: Int = 1850, currentUserName: String = "PuzzleMaster"): List<LeaderboardEntry> {
        val baseList = when (type) {
            LeaderboardType.GLOBAL -> listOf(
                LeaderboardEntry(1, "u1", "PuzzleKing_Alex", "avatar_3", 14500, 182, RankTier.DIAMOND),
                LeaderboardEntry(2, "u2", "SpeedyDragon", "avatar_6", 13200, 160, RankTier.DIAMOND),
                LeaderboardEntry(3, "u3", "GalaxyMaster", "avatar_5", 11800, 134, RankTier.PLATINUM),
                LeaderboardEntry(4, "u4", "SafariLeo", "avatar_4", 9400, 110, RankTier.PLATINUM),
                LeaderboardEntry(5, "u5", "NeonRider", "avatar_1", 8200, 95, RankTier.GOLD),
                LeaderboardEntry(6, "u6", "WonderKid99", "avatar_2", 6700, 78, RankTier.GOLD),
                LeaderboardEntry(7, "local_player", currentUserName, "avatar_1", maxOf(currentUserScore, 3400), 24, RankTier.SILVER, true),
                LeaderboardEntry(8, "u7", "PixelChamp", "avatar_3", 3100, 20, RankTier.SILVER),
                LeaderboardEntry(9, "u8", "TurboBot", "avatar_5", 2200, 14, RankTier.BRONZE),
                LeaderboardEntry(10, "u9", "PuzzleRookie", "avatar_2", 1500, 8, RankTier.BRONZE)
            )
            LeaderboardType.WEEKLY -> listOf(
                LeaderboardEntry(1, "u2", "SpeedyDragon", "avatar_6", 3200, 42, RankTier.DIAMOND),
                LeaderboardEntry(2, "u1", "PuzzleKing_Alex", "avatar_3", 2900, 38, RankTier.DIAMOND),
                LeaderboardEntry(3, "local_player", currentUserName, "avatar_1", maxOf(currentUserScore, 1850), 18, RankTier.SILVER, true),
                LeaderboardEntry(4, "u3", "GalaxyMaster", "avatar_5", 1750, 15, RankTier.PLATINUM),
                LeaderboardEntry(5, "u6", "WonderKid99", "avatar_2", 1400, 12, RankTier.GOLD)
            )
            LeaderboardType.DAILY -> listOf(
                LeaderboardEntry(1, "u5", "NeonRider", "avatar_1", 1480, 1, RankTier.GOLD),
                LeaderboardEntry(2, "u1", "PuzzleKing_Alex", "avatar_3", 1420, 1, RankTier.DIAMOND),
                LeaderboardEntry(3, "local_player", currentUserName, "avatar_1", maxOf(currentUserScore, 1350), 1, RankTier.SILVER, true),
                LeaderboardEntry(4, "u4", "SafariLeo", "avatar_4", 1290, 1, RankTier.PLATINUM),
                LeaderboardEntry(5, "u8", "TurboBot", "avatar_5", 980, 1, RankTier.BRONZE)
            )
            LeaderboardType.FRIENDS -> listOf(
                LeaderboardEntry(1, "local_player", currentUserName, "avatar_1", maxOf(currentUserScore, 3400), 24, RankTier.SILVER, true),
                LeaderboardEntry(2, "f1", "BestFriend_Sam", "avatar_2", 2800, 19, RankTier.SILVER),
                LeaderboardEntry(3, "f2", "GamerCousin", "avatar_4", 1950, 12, RankTier.BRONZE),
                LeaderboardEntry(4, "f3", "PuzzleBuddy", "avatar_5", 1400, 9, RankTier.BRONZE)
            )
        }
        return baseList
    }
}
