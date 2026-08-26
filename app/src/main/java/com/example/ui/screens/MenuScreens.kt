package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserStatsEntity
import com.example.data.model.CategoryGroup
import com.example.data.model.Difficulty
import com.example.data.model.PuzzleCategory
import com.example.data.model.PuzzleItem
import com.example.data.repository.PuzzleCatalog
import com.example.ui.components.GameActionButton
import com.example.ui.components.GameHeaderBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandIndigoDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.KidsCardCyan
import com.example.ui.theme.KidsCardPink
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SecondaryCoral
import com.example.ui.theme.SuccessGreen

@Composable
fun HomeScreen(
    userStats: UserStatsEntity,
    onNavigatePlay: () -> Unit,
    onNavigateMultiplayer: () -> Unit,
    onNavigatePractice: () -> Unit,
    onNavigateDailyChallenge: () -> Unit,
    onNavigateLeaderboard: () -> Unit,
    onNavigateAchievements: () -> Unit,
    onNavigateShop: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateTutorial: () -> Unit,
    onNavigateAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(36.dp))
                // Top Player Status Bar
                GameHeaderBar(
                    username = userStats.username,
                    avatarId = userStats.avatarId,
                    level = userStats.level,
                    coins = userStats.coins,
                    xp = userStats.xp,
                    onProfileClick = onNavigateProfile,
                    onShopClick = onNavigateShop
                )
            }

            // Hero Arena Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .height(140.dp)
                        .testTag("hero_banner"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_battle_banner),
                            contentDescription = "Hero Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            BrandIndigoDark.copy(alpha = 0.85f),
                                            BrandIndigoDark.copy(alpha = 0.35f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AccentGold
                            ) {
                                Text(
                                    text = "SEASON 1 BATTLE PASS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandIndigoDark,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "PICTURE PUZZLE BATTLE",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "BY SOWROV SULTAN",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryCyan
                            )
                        }
                    }
                }
            }

            // Primary Play Action Cards
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Online Multiplayer Battle (Primary Feature)
                    MenuCardLarge(
                        title = "ONLINE MULTIPLAYER",
                        subtitle = "2–4 Players Real-Time Battle Arena",
                        badge = "PVP LIVE",
                        icon = Icons.Default.Groups,
                        gradientColors = listOf(SecondaryCoral, Color(0xFFFF8E53)),
                        onClick = onNavigateMultiplayer,
                        testTag = "btn_nav_multiplayer"
                    )

                    // Quick Play / Mode Select
                    MenuCardLarge(
                        title = "PLAY ADVENTURE",
                        subtitle = "Explore Kids & Pro Puzzle Categories",
                        badge = "SOLO / CO-OP",
                        icon = Icons.Default.PlayArrow,
                        gradientColors = listOf(PrimaryCyan, Color(0xFF0099FF)),
                        onClick = onNavigatePlay,
                        testTag = "btn_nav_play"
                    )

                    // Practice Mode
                    MenuCardLarge(
                        title = "PRACTICE & OFFLINE",
                        subtitle = "No Internet Required • Master All Difficulties",
                        badge = "OFFLINE",
                        icon = Icons.Default.Extension,
                        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)),
                        onClick = onNavigatePractice,
                        testTag = "btn_nav_practice"
                    )
                }
            }

            // Secondary Quick Access Grid
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MenuTileSmall(
                        title = "DAILY QUEST",
                        icon = Icons.Default.Today,
                        color = AccentGold,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateDailyChallenge,
                        testTag = "btn_nav_daily"
                    )
                    MenuTileSmall(
                        title = "LEADERBOARD",
                        icon = Icons.Default.Leaderboard,
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateLeaderboard,
                        testTag = "btn_nav_leaderboard"
                    )
                    MenuTileSmall(
                        title = "SHOP",
                        icon = Icons.Default.ShoppingBag,
                        color = PrimaryCyan,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateShop,
                        testTag = "btn_nav_shop"
                    )
                    MenuTileSmall(
                        title = "AWARDS",
                        icon = Icons.Default.EmojiEvents,
                        color = SecondaryCoral,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateAchievements,
                        testTag = "btn_nav_achievements"
                    )
                }
            }

            // Footer Settings & Tutorial Row
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigateTutorial() }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Tutorial", tint = AccentGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "How to Play",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AccentGold
                        )
                    }

                    Row {
                        IconButton(onClick = onNavigateSettings, modifier = Modifier.testTag("btn_nav_settings")) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                        IconButton(onClick = onNavigateAbout, modifier = Modifier.testTag("btn_nav_about")) {
                            Icon(Icons.Default.Info, contentDescription = "About", tint = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun MenuCardLarge(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "menu_card"
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
        border = BorderStroke(1.dp, BrandBorderDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Subtle gradient accent on right
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                gradientColors.first().copy(alpha = 0.15f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = gradientColors.first().copy(alpha = 0.2f),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = gradientColors.first(),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = gradientColors.first()
                        ) {
                            Text(
                                text = badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF90A4AE)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = gradientColors.first(),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MenuTileSmall(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "menu_tile"
) {
    Card(
        modifier = modifier
            .height(84.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
        border = BorderStroke(1.dp, BrandBorderDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CategorySelectionScreen(
    categories: List<PuzzleCategory>,
    selectedCategory: PuzzleCategory,
    onCategorySelected: (PuzzleCategory) -> Unit,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "SELECT CATEGORY",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Choose your favorite puzzle theme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF90A4AE)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(categories) { cat ->
                    val isSelected = cat.id == selectedCategory.id
                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) PrimaryCyan else BrandBorderDark,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { onCategorySelected(cat) }
                            .testTag("cat_card_${cat.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) BrandCardDark else BrandSurfaceDark
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (cat.group == CategoryGroup.KIDS) KidsCardPink else PrimaryCyan
                            ) {
                                Text(
                                    text = if (cat.group == CategoryGroup.KIDS) "KIDS" else "PRO",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                            Text(
                                text = cat.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            GameActionButton(
                text = "NEXT: DIFFICULTY",
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth(),
                testTag = "cat_continue_btn"
            )
        }
    }
}

@Composable
fun DifficultySelectionScreen(
    difficulties: List<Difficulty>,
    selectedDifficulty: Difficulty,
    onDifficultySelected: (Difficulty) -> Unit,
    onStartGame: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "SELECT DIFFICULTY",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Choose grid size & piece count",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF90A4AE)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(difficulties) { diff ->
                    val isSelected = diff == selectedDifficulty

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(82.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) AccentGold else BrandBorderDark,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { onDifficultySelected(diff) }
                            .testTag("diff_${diff.name}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) BrandCardDark else BrandSurfaceDark
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = diff.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (diff.isKidsFriendly) SuccessGreen.copy(alpha = 0.2f)
                                        else SecondaryCoral.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "${diff.pieceCount} PIECES",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (diff.isKidsFriendly) SuccessGreen else SecondaryCoral,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Timer: ${diff.timeSeconds}s • Score Bonus: x${diff.multiplier}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF90A4AE)
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = AccentGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            GameActionButton(
                text = "START PUZZLE",
                onClick = onStartGame,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth(),
                testTag = "start_game_btn"
            )
        }
    }
}
