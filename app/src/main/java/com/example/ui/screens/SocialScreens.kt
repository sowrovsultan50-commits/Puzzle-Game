package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserStatsEntity
import com.example.data.model.RankTier
import com.example.data.model.ShopItem
import com.example.data.repository.GameExtrasCatalog
import com.example.data.repository.LeaderboardEntry
import com.example.data.repository.LeaderboardType
import com.example.data.repository.PuzzleCatalog
import com.example.ui.components.AvatarIcon
import com.example.ui.components.GameActionButton
import com.example.ui.components.GameHeaderBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandIndigoDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.RankBronze
import com.example.ui.theme.RankDiamond
import com.example.ui.theme.RankGold
import com.example.ui.theme.RankPlatinum
import com.example.ui.theme.RankSilver
import com.example.ui.theme.SecondaryCoral
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.MainViewModel

@Composable
fun LeaderboardScreen(
    mainViewModel: MainViewModel,
    onBack: () -> Unit
) {
    val currentType by mainViewModel.leaderboardType.collectAsState()
    val entries = mainViewModel.getLeaderboardEntries()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "LEADERBOARD",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Rankings & Global Champions",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF90A4AE)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tabs (Global, Weekly, Daily, Friends)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LeaderboardType.entries.forEach { type ->
                    val isSelected = type == currentType
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { mainViewModel.setLeaderboardType(type) }
                            .testTag("tab_${type.name}"),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) PrimaryCyan else BrandCardDark,
                        border = BorderStroke(1.dp, if (isSelected) PrimaryCyan else BrandBorderDark)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = type.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BrandIndigoDark else Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Leaderboard list
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries) { entry ->
                    LeaderboardRowItem(entry = entry)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            GameActionButton(
                text = "MAIN MENU",
                onClick = onBack,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                modifier = Modifier.fillMaxWidth(),
                testTag = "leaderboard_back_btn"
            )
        }
    }
}

@Composable
fun LeaderboardRowItem(entry: LeaderboardEntry) {
    val rankBadgeColor = when (entry.rank) {
        1 -> RankGold
        2 -> RankSilver
        3 -> RankBronze
        else -> Color(0xFF546E7A)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isCurrentUser) PrimaryCyan.copy(alpha = 0.15f) else BrandSurfaceDark
        ),
        border = BorderStroke(
            width = if (entry.isCurrentUser) 1.5.dp else 1.dp,
            color = if (entry.isCurrentUser) PrimaryCyan else BrandBorderDark
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rank position
                Surface(
                    shape = CircleShape,
                    color = rankBadgeColor.copy(alpha = 0.2f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${entry.rank}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = rankBadgeColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
                AvatarIcon(avatarId = entry.avatarId, size = 36.dp, rank = entry.tier)
                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = entry.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (entry.isCurrentUser) PrimaryCyan else Color.White
                    )
                    Text(
                        text = "${entry.wins} Wins • ${entry.tier.title}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF90A4AE)
                    )
                }
            }

            Text(
                text = "${entry.score} PTS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = AccentGold
            )
        }
    }
}

@Composable
fun AchievementsScreen(
    onBack: () -> Unit
) {
    val achievements = GameExtrasCatalog.achievements

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "ACHIEVEMENTS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Earn badges & free bonus coins",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF90A4AE)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(achievements) { ach ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                        border = BorderStroke(
                            1.dp,
                            if (ach.isUnlocked) AccentGold.copy(alpha = 0.6f) else BrandBorderDark
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (ach.isUnlocked) AccentGold.copy(alpha = 0.2f) else BrandCardDark,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (ach.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = if (ach.isUnlocked) AccentGold else Color(0xFF546E7A),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = ach.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = ach.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF90A4AE)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (ach.isUnlocked) SuccessGreen.copy(alpha = 0.2f) else AccentGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (ach.isUnlocked) "UNLOCKED" else "+${ach.coinReward} COINS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (ach.isUnlocked) SuccessGreen else AccentGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            GameActionButton(
                text = "MAIN MENU",
                onClick = onBack,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                modifier = Modifier.fillMaxWidth(),
                testTag = "achievements_back_btn"
            )
        }
    }
}

@Composable
fun DailyChallengeScreen(
    userStats: UserStatsEntity,
    isCompletedToday: Boolean,
    onStartChallenge: () -> Unit,
    onBack: () -> Unit
) {
    val dailyPuzzle = PuzzleCatalog.getDailyPuzzle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AccentGold
                ) {
                    Text(
                        text = "TODAY'S SPECIAL QUEST",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandIndigoDark,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "DAILY PUZZLE QUEST",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "1 Daily Attempt • Extra XP & 200 Gold Bonus",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.5.dp, AccentGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AvatarIcon(avatarId = "avatar_5", size = 64.dp, rank = RankTier.GOLD)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = dailyPuzzle.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Difficulty: ${dailyPuzzle.defaultDifficulty.title} (${dailyPuzzle.defaultDifficulty.pieceCount} Pieces)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PrimaryCyan
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AccentGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "+200 Bonus Coins",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AccentGold,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PrimaryCyan.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "+300 Quest XP",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PrimaryCyan,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                GameActionButton(
                    text = if (isCompletedToday) "ALREADY COMPLETED TODAY" else "PLAY DAILY CHALLENGE",
                    onClick = onStartChallenge,
                    enabled = !isCompletedToday,
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "daily_start_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                GameActionButton(
                    text = "MAIN MENU",
                    onClick = onBack,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "daily_back_btn"
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    userStats: UserStatsEntity,
    onSaveProfile: (name: String, avatar: String) -> Unit,
    onBack: () -> Unit
) {
    var username by remember { mutableStateOf(userStats.username) }
    var selectedAvatar by remember { mutableStateOf(userStats.avatarId) }

    val currentRank = RankTier.fromXp(userStats.xp)
    val nextTier = RankTier.entries.firstOrNull { it.minXp > userStats.xp } ?: RankTier.DIAMOND
    val xpProgress = (userStats.xp.toFloat() / nextTier.minXp).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "PLAYER PROFILE",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Your battle statistics and ranks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Avatar display
                AvatarIcon(avatarId = selectedAvatar, size = 80.dp, rank = currentRank)

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar picker
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GameExtrasCatalog.avatars.forEach { avId ->
                        val isSel = selectedAvatar == avId
                        Box(
                            modifier = Modifier
                                .scale(if (isSel) 1.15f else 1.0f)
                                .clickable { selectedAvatar = avId }
                        ) {
                            AvatarIcon(avatarId = avId, size = 44.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { if (it.length <= 15) username = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PrimaryCyan,
                        unfocusedBorderColor = BrandBorderDark,
                        focusedLabelColor = PrimaryCyan,
                        unfocusedLabelColor = Color(0xFF90A4AE)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Rank and XP Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Rank Tier: ${currentRank.title}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "LVL ${userStats.level}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = PrimaryCyan)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { xpProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PrimaryCyan,
                            trackColor = BrandCardDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${userStats.xp} / ${nextTier.minXp} XP to ${nextTier.title}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Battle Statistics Grid
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "COMBAT STATS", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryCyan)
                        Spacer(modifier = Modifier.height(10.dp))
                        StatItem(title = "Total Matches", value = "${userStats.totalGames}")
                        StatItem(title = "Victories (Wins)", value = "${userStats.wins}")
                        StatItem(title = "Losses", value = "${userStats.losses}")
                        StatItem(title = "Best Match Score", value = "${userStats.bestScore} PTS")
                        StatItem(title = "Best Time", value = "${String.format("%.1f", userStats.bestTimeSeconds)}s")
                        StatItem(title = "Puzzles Cleared", value = "${userStats.puzzlesCompletedCount}")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GameActionButton(
                    text = "SAVE CHANGES",
                    onClick = { onSaveProfile(username, selectedAvatar) },
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "save_profile_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                GameActionButton(
                    text = "BACK",
                    onClick = onBack,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "profile_back_btn"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF90A4AE))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun ShopScreen(
    userCoins: Int,
    onBuyItem: (ShopItem) -> Unit,
    onWatchAd: () -> Unit,
    onBack: () -> Unit
) {
    val items = GameExtrasCatalog.shopItems

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "COIN SHOP", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color.White)
                    Text(text = "Unlock avatars, themes & hints", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF90A4AE))
                }
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandCardDark,
                    border = BorderStroke(1.dp, AccentGold)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "$userCoins", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AccentGold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rewarded Video Ad banner for Free Gold
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onWatchAd() }
                    .testTag("watch_rewarded_ad_btn"),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                border = BorderStroke(1.dp, AccentGold)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = AccentGold.copy(alpha = 0.2f), modifier = Modifier.size(42.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Videocam, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Watch Video for +50 Gold", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Free bonus coins available now", style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                        }
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = AccentGold) {
                        Text(text = "FREE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = BrandIndigoDark, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { item ->
                    val canAfford = userCoins >= item.priceCoins
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                        border = BorderStroke(1.dp, BrandBorderDark)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(shape = CircleShape, color = PrimaryCyan.copy(alpha = 0.2f), modifier = Modifier.size(42.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = PrimaryCyan, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(text = item.type.name, style = MaterialTheme.typography.labelSmall, color = Color(0xFF90A4AE))
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = canAfford) { onBuyItem(item) }
                                    .testTag("buy_item_${item.id}"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (canAfford) AccentGold else Color(0xFF37474F)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = if (canAfford) BrandIndigoDark else Color(0xFF90A4AE), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${item.priceCoins}", style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp), fontWeight = FontWeight.Black, color = if (canAfford) BrandIndigoDark else Color(0xFF90A4AE))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            GameActionButton(
                text = "MAIN MENU",
                onClick = onBack,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                modifier = Modifier.fillMaxWidth(),
                testTag = "shop_back_btn"
            )
        }
    }
}
