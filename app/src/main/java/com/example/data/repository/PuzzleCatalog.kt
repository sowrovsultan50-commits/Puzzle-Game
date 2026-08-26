package com.example.data.repository

import com.example.R
import com.example.data.model.CategoryGroup
import com.example.data.model.Difficulty
import com.example.data.model.PuzzleCategory
import com.example.data.model.PuzzleItem

object PuzzleCatalog {

    val categories: List<PuzzleCategory> = listOf(
        // Kids Categories
        PuzzleCategory("cat_animals", "Animals", CategoryGroup.KIDS, "Pets", "Cute and friendly animals"),
        PuzzleCategory("cat_cartoons", "Cartoons", CategoryGroup.KIDS, "Face", "Fun cartoon characters"),
        PuzzleCategory("cat_fruits", "Fruits", CategoryGroup.KIDS, "Eco", "Delicious colorful fruits"),
        PuzzleCategory("cat_vehicles", "Vehicles", CategoryGroup.KIDS, "DirectionsCar", "Cars, trains, and planes"),
        PuzzleCategory("cat_dinosaurs", "Dinosaurs", CategoryGroup.KIDS, "AutoAwesome", "Prehistoric dino adventures"),
        PuzzleCategory("cat_space", "Space", CategoryGroup.KIDS, "RocketLaunch", "Cosmic stars and rockets"),
        PuzzleCategory("cat_ocean", "Ocean", CategoryGroup.KIDS, "WaterDrop", "Deep sea creatures"),
        PuzzleCategory("cat_toys", "Toys", CategoryGroup.KIDS, "SmartToy", "Playful blocks and toys"),
        PuzzleCategory("cat_fantasy", "Fantasy", CategoryGroup.KIDS, "Castle", "Magical dragons and castles"),

        // General Categories
        PuzzleCategory("cat_nature", "Nature", CategoryGroup.GENERAL, "Forest", "Breathtaking landscapes"),
        PuzzleCategory("cat_cities", "Cities & Landmarks", CategoryGroup.GENERAL, "LocationCity", "Famous world wonders"),
        PuzzleCategory("cat_sports", "Sports", CategoryGroup.GENERAL, "SportsSoccer", "Thrilling athletics"),
        PuzzleCategory("cat_tech", "Technology", CategoryGroup.GENERAL, "Computer", "Futuristic gadgets"),
        PuzzleCategory("cat_food", "Food & Travel", CategoryGroup.GENERAL, "Restaurant", "Gourmet culinary delights")
    )

    val puzzles: List<PuzzleItem> = listOf(
        PuzzleItem(
            id = "puz_safari_animals",
            title = "Jungle Safari Friends",
            categoryId = "cat_animals",
            drawableRes = R.drawable.img_puzzle_safari_animals,
            isKids = true,
            defaultDifficulty = Difficulty.EASY_3X3,
            tags = listOf("lion", "elephant", "giraffe", "safari")
        ),
        PuzzleItem(
            id = "puz_cosmic_space",
            title = "Cosmic Galaxy Voyager",
            categoryId = "cat_space",
            drawableRes = R.drawable.img_puzzle_cosmic_space,
            isKids = true,
            defaultDifficulty = Difficulty.MEDIUM_4X4,
            tags = listOf("astronaut", "space", "nebula", "stars")
        ),
        PuzzleItem(
            id = "puz_fantasy_dragon",
            title = "Magical Emerald Dragon",
            categoryId = "cat_fantasy",
            drawableRes = R.drawable.img_puzzle_fantasy_dragon,
            isKids = true,
            defaultDifficulty = Difficulty.HARD_5X5,
            tags = listOf("dragon", "castle", "fantasy", "magic")
        ),
        PuzzleItem(
            id = "puz_battle_arena",
            title = "Champion Puzzle Arena",
            categoryId = "cat_cities",
            drawableRes = R.drawable.img_hero_battle_banner,
            isKids = false,
            defaultDifficulty = Difficulty.HARD_6X6,
            tags = listOf("arena", "championship", "battle", "pro")
        ),
        PuzzleItem(
            id = "puz_puzzle_emblem",
            title = "Master Emblem Clash",
            categoryId = "cat_tech",
            drawableRes = R.drawable.img_app_icon,
            isKids = false,
            defaultDifficulty = Difficulty.EXPERT_8X8,
            tags = listOf("icon", "emblem", "gold", "master")
        )
    )

    fun getPuzzleById(id: String): PuzzleItem {
        return puzzles.firstOrNull { it.id == id } ?: puzzles.first()
    }

    fun getPuzzlesForCategory(categoryId: String): List<PuzzleItem> {
        val filtered = puzzles.filter { it.categoryId == categoryId }
        return if (filtered.isNotEmpty()) filtered else listOf(puzzles.first())
    }

    fun getDailyPuzzle(): PuzzleItem {
        // Daily puzzle rotation based on day of year
        val dayIndex = (System.currentTimeMillis() / (1000 * 60 * 60 * 24)).toInt()
        val index = (dayIndex % puzzles.size).coerceAtLeast(0)
        return puzzles[index]
    }
}
