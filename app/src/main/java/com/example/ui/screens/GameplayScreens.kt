package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Difficulty
import com.example.data.model.MatchScoreBreakdown
import com.example.data.model.PlayerInfo
import com.example.data.model.RoomInfo
import com.example.domain.engine.PuzzleEngine
import com.example.ui.components.AvatarIcon
import com.example.ui.components.GameActionButton
import com.example.ui.components.OpponentLiveProgressBar
import com.example.ui.components.PuzzleBoardView
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandIndigoDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.RankBronze
import com.example.ui.theme.RankGold
import com.example.ui.theme.RankSilver
import com.example.ui.theme.SecondaryCoral
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange
import com.example.viewmodel.GameViewModel

@Composable
fun ActivePuzzleGameScreen(
    gameViewModel: GameViewModel,
    onMatchFinished: () -> Unit,
    onExitGame: () -> Unit
) {
    val puzzleItem by gameViewModel.currentPuzzle.collectAsState()
    val difficulty by gameViewModel.difficulty.collectAsState()
    val engine by gameViewModel.puzzleEngine.collectAsState()
    val timeRemaining by gameViewModel.timeRemainingSeconds.collectAsState()
    val isGameActive by gameViewModel.isGameActive.collectAsState()
    val isPaused by gameViewModel.isPaused.collectAsState()
    val isMultiplayer by gameViewModel.isMultiplayer.collectAsState()
    val currentRoom by gameViewModel.currentRoom.collectAsState()
    val showHintDialog by gameViewModel.showHintDialog.collectAsState()
    val matchResult by gameViewModel.matchResult.collectAsState()

    // If match ended
    if (matchResult != null) {
        GameFinishedScreen(
            result = matchResult!!,
            puzzleTitle = puzzleItem.title,
            isMultiplayer = isMultiplayer,
            room = currentRoom,
            onRematch = {
                if (isMultiplayer) {
                    gameViewModel.requestMultiplayerRematch()
                } else {
                    gameViewModel.restartMatch()
                }
            },
            onMainMenu = onExitGame
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP HUD Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                border = BorderStroke(1.dp, BrandBorderDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Pause Button
                    IconButton(
                        onClick = { gameViewModel.togglePause() },
                        modifier = Modifier.testTag("pause_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause",
                            tint = Color.White
                        )
                    }

                    // Timer Display
                    val timerColor = when {
                        timeRemaining <= 15 -> ErrorRed
                        timeRemaining <= 30 -> WarningOrange
                        else -> PrimaryCyan
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(BrandCardDark, RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = timerColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = timerColor
                        )
                    }

                    // Hint Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AccentGold.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { gameViewModel.openHintDialog() }
                            .testTag("hint_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Hints",
                                tint = AccentGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "HINT",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                        }
                    }
                }
            }

            // Multiplayer Opponent Progress HUD (If in 2-4 player multiplayer match)
            if (isMultiplayer && currentRoom != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark.copy(alpha = 0.8f))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        currentRoom!!.players.values.forEach { opponent ->
                            OpponentLiveProgressBar(player = opponent)
                        }
                    }
                }
            }

            // Interactive Puzzle Board & Piece Tray
            if (engine != null) {
                PuzzleBoardView(
                    puzzleEngine = engine!!,
                    drawableRes = puzzleItem.drawableRes,
                    onPiecePlaced = { pieceId, targetSlot ->
                        gameViewModel.handlePiecePlacement(pieceId, targetSlot)
                    },
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }

        // Hint Options Dialog
        if (showHintDialog) {
            HintDialog(
                onHighlightHint = { gameViewModel.useHighlightHint() },
                onGhostHint = { gameViewModel.useGhostHint() },
                onAutoPlaceHint = { gameViewModel.useAutoPlaceHint() },
                onDismiss = { gameViewModel.closeHintDialog() }
            )
        }

        // Pause Overlay Dialog
        if (isPaused) {
            PauseDialog(
                onResume = { gameViewModel.togglePause() },
                onRestart = {
                    gameViewModel.togglePause()
                    gameViewModel.restartMatch()
                },
                onQuit = onExitGame
            )
        }
    }
}

@Composable
fun HintDialog(
    onHighlightHint: () -> Unit,
    onGhostHint: () -> Unit,
    onAutoPlaceHint: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
            border = BorderStroke(1.dp, BrandBorderDark)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PUZZLE ASSIST",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = AccentGold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hint 1: Highlight slot
                HintOptionCard(
                    title = "Highlight Slot",
                    desc = "Shows exact target slot for piece",
                    cost = 15,
                    icon = Icons.Default.Lightbulb,
                    onClick = onHighlightHint,
                    testTag = "hint_highlight_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Hint 2: Ghost Image Overlay
                HintOptionCard(
                    title = "Ghost Guide Overlay",
                    desc = "Displays faint transparent background",
                    cost = 25,
                    icon = Icons.Default.Visibility,
                    onClick = onGhostHint,
                    testTag = "hint_ghost_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Hint 3: Auto Place Piece
                HintOptionCard(
                    title = "Auto-Place 1 Piece",
                    desc = "Instantly snaps a piece into correct slot",
                    cost = 50,
                    icon = Icons.Default.AutoAwesome,
                    onClick = onAutoPlaceHint,
                    testTag = "hint_autoplace_btn"
                )
            }
        }
    }
}

@Composable
fun HintOptionCard(
    title: String,
    desc: String,
    cost: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = BrandCardDark,
        border = BorderStroke(1.dp, BrandBorderDark)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(imageVector = icon, contentDescription = null, tint = PrimaryCyan, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF90A4AE))
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AccentGold.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$cost", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = AccentGold)
                }
            }
        }
    }
}

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onQuit: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
            border = BorderStroke(1.dp, BrandBorderDark)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GAME PAUSED",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                GameActionButton(
                    text = "RESUME",
                    onClick = onResume,
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "resume_btn"
                )

                Spacer(modifier = Modifier.height(12.dp))

                GameActionButton(
                    text = "RESTART",
                    onClick = onRestart,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    icon = Icons.Default.Replay,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "restart_btn"
                )

                Spacer(modifier = Modifier.height(12.dp))

                GameActionButton(
                    text = "QUIT TO MENU",
                    onClick = onQuit,
                    gradientColors = listOf(SecondaryCoral, Color(0xFFC62828)),
                    icon = Icons.Default.Close,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "quit_btn"
                )
            }
        }
    }
}

@Composable
fun GameFinishedScreen(
    result: MatchScoreBreakdown,
    puzzleTitle: String,
    isMultiplayer: Boolean,
    room: RoomInfo?,
    onRematch: () -> Unit,
    onMainMenu: () -> Unit
) {
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
                Spacer(modifier = Modifier.height(28.dp))

                // Victory Header
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentGold
                ) {
                    Text(
                        text = if (result.rankPosition == 1) "VICTORY!" else "MATCH FINISHED",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = BrandIndigoDark,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3-Star Rating
                val stars = when {
                    result.finalScore >= 1400 -> 3
                    result.finalScore >= 900 -> 2
                    else -> 1
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (s in 1..3) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $s",
                            tint = if (s <= stars) AccentGold else Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = puzzleTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total Final Score Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.5.dp, PrimaryCyan)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TOTAL SCORE",
                            style = MaterialTheme.typography.labelMedium,
                            color = PrimaryCyan,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "${result.finalScore}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Rewards row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AccentGold.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "+${result.coinsEarned} Coins", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = AccentGold)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PrimaryCyan.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = PrimaryCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "+${result.xpEarned} XP", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryCyan)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Score Breakdown Table
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "SCORE BREAKDOWN",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ScoreRow(label = "Base Clear Score", value = "+${result.baseScore}")
                        ScoreRow(label = "Speed Bonus (${String.format("%.1f", result.timeTakenSeconds)}s)", value = "+${result.speedBonus}")
                        ScoreRow(label = "Accuracy Bonus", value = "+${result.accuracyBonus}")
                        ScoreRow(label = "Difficulty Bonus", value = "+${result.difficultyBonus}")
                        if (result.wrongPenalty > 0) {
                            ScoreRow(label = "Wrong Moves (${result.wrongMoves})", value = "-${result.wrongPenalty}", isPenalty = true)
                        }
                        if (result.hintPenalty > 0) {
                            ScoreRow(label = "Hints Used (${result.hintsUsed})", value = "-${result.hintPenalty}", isPenalty = true)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                GameActionButton(
                    text = if (isMultiplayer) "REQUEST REMATCH" else "PLAY AGAIN",
                    onClick = onRematch,
                    icon = Icons.Default.Replay,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "results_rematch_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                GameActionButton(
                    text = "MAIN MENU",
                    onClick = onMainMenu,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "results_menu_btn"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ScoreRow(label: String, value: String, isPenalty: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFB0BEC5))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isPenalty) ErrorRed else SuccessGreen
        )
    }
}
