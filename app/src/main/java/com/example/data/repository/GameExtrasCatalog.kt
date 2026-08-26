package com.example.data.repository

import com.example.data.model.AchievementItem
import com.example.data.model.ShopItem
import com.example.data.model.ShopItemType

object GameExtrasCatalog {

    val achievements: List<AchievementItem> = listOf(
        AchievementItem("ach_first_win", "First Victory", "Win your first picture puzzle match", "EmojiEvents", 1, 1, true, 100),
        AchievementItem("ach_puzzle_master", "Puzzle Master", "Complete 25 total picture puzzles", "Extension", 25, 3, false, 250),
        AchievementItem("ach_speed_demon", "Speed Demon", "Solve any medium or hard puzzle under 45s", "Speed", 1, 0, false, 200),
        AchievementItem("ach_10_wins", "Veteran Battler", "Win 10 online multiplayer battles", "MilitaryTech", 10, 2, false, 300),
        AchievementItem("ach_50_wins", "Grand Champion", "Win 50 online multiplayer battles", "WorkspacePremium", 50, 2, false, 1000),
        AchievementItem("ach_perfect_puzzle", "Flawless Assembly", "Complete a puzzle with zero wrong moves", "AutoFixHigh", 1, 0, false, 200),
        AchievementItem("ach_no_hint", "Pure Intuition", "Win a hard match without using any hints", "VisibilityOff", 1, 0, false, 250),
        AchievementItem("ach_multi_champ", "Multiplayer Ace", "Win a 4-player multiplayer battle", "Groups", 1, 0, false, 300),
        AchievementItem("ach_daily_player", "Daily Devotee", "Complete 5 Daily Puzzle Challenges", "Today", 5, 1, false, 400)
    )

    val shopItems: List<ShopItem> = listOf(
        ShopItem("shop_avatar_dragon", "Dragon Hero Avatar", ShopItemType.AVATAR, 200, false, "avatar_dragon"),
        ShopItem("shop_avatar_astronaut", "Cosmic Voyager Avatar", ShopItemType.AVATAR, 250, false, "avatar_astronaut"),
        ShopItem("shop_avatar_lion", "Safari King Avatar", ShopItemType.AVATAR, 300, false, "avatar_lion"),
        ShopItem("shop_avatar_robot", "Cyber Bot Avatar", ShopItemType.AVATAR, 350, false, "avatar_robot"),
        ShopItem("shop_hints_5", "5x Smart Hints Pack", ShopItemType.HINT_PACK, 100, false, "hint_pack_5"),
        ShopItem("shop_hints_15", "15x Mega Hints Pack", ShopItemType.HINT_PACK, 250, false, "hint_pack_15"),
        ShopItem("shop_theme_neon", "Cyber Neon Arena Theme", ShopItemType.THEME, 500, false, "theme_neon"),
        ShopItem("shop_theme_pastel", "Kids Sunshine Theme", ShopItemType.THEME, 400, false, "theme_pastel"),
        ShopItem("shop_pack_dinosaurs", "Prehistoric Dino Pack", ShopItemType.PUZZLE_PACK, 600, false, "pack_dino"),
        ShopItem("shop_pack_landmarks", "World Wonders Pack", ShopItemType.PUZZLE_PACK, 700, false, "pack_landmarks")
    )

    val avatars: List<String> = listOf(
        "avatar_1", // Default boy
        "avatar_2", // Default girl
        "avatar_3", // Cool gamer
        "avatar_4", // Cute lion
        "avatar_5", // Astronaut
        "avatar_6"  // Dragon
    )
}
