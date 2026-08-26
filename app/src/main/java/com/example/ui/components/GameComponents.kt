package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerInfo
import com.example.data.model.RankTier
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

@Composable
fun GameHeaderBar(
    username: String,
    avatarId: String,
    level: Int,
    coins: Int,
    xp: Int,
    onProfileClick: () -> Unit,
    onShopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark.copy(alpha = 0.95f)),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, BrandBorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile & Level
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onProfileClick() }
                    .padding(4.dp)
            ) {
                AvatarIcon(avatarId = avatarId, size = 42.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryCyan.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "LVL $level",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryCyan,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$xp XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFB0BEC5)
                        )
                    }
                }
            }

            // Coin Balance Pill
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = BrandCardDark,
                border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.6f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onShopClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$coins",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )
                }
            }
        }
    }
}

@Composable
fun GameActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    gradientColors: List<Color> = listOf(PrimaryCyan, Color(0xFF0099FF)),
    textColor: Color = Color.White,
    height: Int = 54,
    enabled: Boolean = true,
    testTag: String = "game_action_btn"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = tween(100),
        label = "btn_scale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .height(height.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = if (enabled) 6.dp else 0.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (enabled) Brush.horizontalGradient(gradientColors)
                    else Brush.horizontalGradient(listOf(Color(0xFF475569), Color(0xFF334155)))
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun AvatarIcon(
    avatarId: String,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    rank: RankTier = RankTier.BRONZE,
    modifier: Modifier = Modifier
) {
    val (icon, bgGradient) = when (avatarId) {
        "avatar_2" -> Pair(Icons.Default.Face, listOf(Color(0xFFFF4081), Color(0xFFFF80AB)))
        "avatar_3" -> Pair(Icons.Default.Bolt, listOf(Color(0xFF7C4DFF), Color(0xFFB388FF)))
        "avatar_4" -> Pair(Icons.Default.Pets, listOf(Color(0xFFFF6D00), Color(0xFFFFAB40)))
        "avatar_5" -> Pair(Icons.Default.RocketLaunch, listOf(Color(0xFF00E5FF), Color(0xFF18FFFF)))
        "avatar_6" -> Pair(Icons.Default.AutoAwesome, listOf(Color(0xFF00E676), Color(0xFF69F0AE)))
        else -> Pair(Icons.Default.AccountCircle, listOf(Color(0xFF2979FF), Color(0xFF82B1FF)))
    }

    val rankBorderColor = when (rank) {
        RankTier.BRONZE -> RankBronze
        RankTier.SILVER -> RankSilver
        RankTier.GOLD -> RankGold
        RankTier.PLATINUM -> RankPlatinum
        RankTier.DIAMOND -> RankDiamond
    }

    Box(
        modifier = modifier
            .size(size)
            .border(2.dp, rankBorderColor, CircleShape)
            .clip(CircleShape)
            .background(Brush.linearGradient(bgGradient)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Avatar",
            tint = Color.White,
            modifier = Modifier.size(size * 0.65f)
        )
    }
}

@Composable
fun OpponentLiveProgressBar(
    player: PlayerInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarIcon(avatarId = player.avatarId, size = 32.dp, rank = player.rank)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (player.isFinished) "FINISHED!" else "${player.progressPercent}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (player.isFinished) SuccessGreen else PrimaryCyan
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { player.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (player.isFinished) SuccessGreen else PrimaryCyan,
                trackColor = BrandBorderDark
            )
        }
    }
}

@Composable
fun MatchCountdownOverlay(
    secondsRemaining: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "countdown_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MATCH STARTING IN",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.8f),
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (secondsRemaining > 0) "$secondsRemaining" else "GO!",
                fontSize = 90.sp,
                fontWeight = FontWeight.Black,
                color = if (secondsRemaining > 0) AccentGold else SuccessGreen,
                modifier = Modifier.scale(scale)
            )
        }
    }
}

@Composable
fun ConnectionStatusBanner(
    isConnecting: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isConnecting || errorMessage != null,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (errorMessage != null) ErrorRed.copy(alpha = 0.9f)
                else BrandSurfaceDark.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (errorMessage != null) Icons.Default.WifiOff else Icons.Default.RocketLaunch,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = errorMessage ?: "Connecting to multiplayer battle server...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                if (errorMessage != null) {
                    IconButton(onClick = onRetry) {
                        Icon(Icons.Default.Bolt, contentDescription = "Retry", tint = AccentGold)
                    }
                }
            }
        }
    }
}
