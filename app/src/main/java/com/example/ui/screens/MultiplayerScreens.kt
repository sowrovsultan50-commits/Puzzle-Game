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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerInfo
import com.example.data.model.RoomInfo
import com.example.data.model.RoomState
import com.example.ui.components.AvatarIcon
import com.example.ui.components.GameActionButton
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandIndigoDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SecondaryCoral
import com.example.ui.theme.SuccessGreen

@Composable
fun MultiplayerMenuScreen(
    onQuickMatchSelected: (playerCount: Int) -> Unit,
    onCreateRoomSelected: () -> Unit,
    onJoinRoomSelected: () -> Unit,
    onBack: () -> Unit
) {
    var selectedPlayerCount by remember { mutableStateOf(2) }

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
                Text(
                    text = "MULTIPLAYER ARENA",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Battle 2–4 players online in real time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Quick Match Player Count Selector
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "QUICK MATCH BATTLE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                        Text(
                            text = "Select number of players for matchmaking",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF90A4AE)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(2, 3, 4).forEach { count ->
                                val isSelected = selectedPlayerCount == count
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedPlayerCount = count }
                                        .testTag("quick_match_${count}p"),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) PrimaryCyan else BrandCardDark,
                                    border = BorderStroke(1.dp, if (isSelected) PrimaryCyan else BrandBorderDark)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "$count PLAYERS",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Black,
                                            color = if (isSelected) BrandIndigoDark else Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        GameActionButton(
                            text = "FIND $selectedPlayerCount-PLAYER MATCH",
                            onClick = { onQuickMatchSelected(selectedPlayerCount) },
                            icon = Icons.Default.SportsEsports,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "find_match_btn"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Private Room Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Create Room
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onCreateRoomSelected() }
                            .testTag("create_room_btn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                        border = BorderStroke(1.dp, BrandBorderDark)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = "Create Room",
                                tint = AccentGold,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Create Room",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Host with 6-digit PIN",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF90A4AE)
                            )
                        }
                    }

                    // Join Room
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onJoinRoomSelected() }
                            .testTag("join_room_btn"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                        border = BorderStroke(1.dp, BrandBorderDark)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MeetingRoom,
                                contentDescription = "Join Room",
                                tint = SuccessGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Join Room",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Enter friend's code",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF90A4AE)
                            )
                        }
                    }
                }
            }

            // Back button
            GameActionButton(
                text = "MAIN MENU",
                onClick = onBack,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                modifier = Modifier.fillMaxWidth(),
                testTag = "multiplayer_back_btn"
            )
        }
    }
}

@Composable
fun JoinRoomScreen(
    onJoinSubmitted: (roomCode: String) -> Unit,
    onBack: () -> Unit
) {
    var roomCode by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "JOIN PRIVATE ROOM",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Enter the 6-digit match code from host",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF90A4AE)
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = roomCode,
                onValueChange = { if (it.length <= 6) roomCode = it },
                label = { Text("6-Digit Room Code") },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center,
                    letterSpacing = 4.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("room_code_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = PrimaryCyan,
                    unfocusedBorderColor = BrandBorderDark,
                    focusedLabelColor = PrimaryCyan,
                    unfocusedLabelColor = Color(0xFF90A4AE)
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            GameActionButton(
                text = "JOIN MATCH",
                onClick = { onJoinSubmitted(roomCode) },
                enabled = roomCode.length >= 4,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth(),
                testTag = "submit_join_btn"
            )

            Spacer(modifier = Modifier.height(12.dp))

            GameActionButton(
                text = "CANCEL",
                onClick = onBack,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                modifier = Modifier.fillMaxWidth(),
                testTag = "cancel_join_btn"
            )
        }
    }
}

@Composable
fun RoomLobbyScreen(
    room: RoomInfo,
    currentUserId: String,
    onToggleReady: () -> Unit,
    onStartMatch: () -> Unit,
    onLeaveRoom: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val isHost = room.hostId == currentUserId
    val myPlayer = room.players[currentUserId]
    val isReady = myPlayer?.isReady == true

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

                // Room Code Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "ROOM CODE",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = room.roomId,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Black,
                                color = AccentGold,
                                letterSpacing = 3.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(room.roomId))
                            },
                            modifier = Modifier.testTag("copy_room_code")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Code",
                                tint = PrimaryCyan
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "PLAYERS IN LOBBY (${room.players.size}/${room.maxPlayers})",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2 to 4 Player Slots Grid
                val playerList = room.players.values.toList()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (i in 0 until room.maxPlayers) {
                        val player = playerList.getOrNull(i)
                        item {
                            LobbyPlayerSlotCard(player = player, slotNumber = i + 1)
                        }
                    }
                }
            }

            // Bottom Actions
            Column(modifier = Modifier.fillMaxWidth()) {
                if (isHost) {
                    val canStart = room.players.size >= 2
                    GameActionButton(
                        text = if (canStart) "START BATTLE" else "WAITING FOR PLAYERS...",
                        onClick = onStartMatch,
                        enabled = canStart,
                        icon = Icons.Default.PlayArrow,
                        gradientColors = listOf(SuccessGreen, Color(0xFF00B0FF)),
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "host_start_match_btn"
                    )
                } else {
                    GameActionButton(
                        text = if (isReady) "READY (TAP TO UNREADY)" else "TAP TO READY",
                        onClick = onToggleReady,
                        gradientColors = if (isReady) listOf(SuccessGreen, Color(0xFF00C853))
                        else listOf(PrimaryCyan, Color(0xFF0099FF)),
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "player_ready_btn"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                GameActionButton(
                    text = "LEAVE ROOM",
                    onClick = onLeaveRoom,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "leave_room_btn"
                )
            }
        }
    }
}

@Composable
fun LobbyPlayerSlotCard(
    player: PlayerInfo?,
    slotNumber: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (player != null) BrandSurfaceDark else BrandCardDark.copy(alpha = 0.4f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (player?.isReady == true) SuccessGreen else BrandBorderDark
        )
    ) {
        if (player != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AvatarIcon(avatarId = player.avatarId, size = 42.dp, rank = player.rank)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (player.isHost) AccentGold
                    else if (player.isReady) SuccessGreen
                    else Color(0xFF546E7A)
                ) {
                    Text(
                        text = if (player.isHost) "HOST" else if (player.isReady) "READY" else "NOT READY",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        fontWeight = FontWeight.Black,
                        color = BrandIndigoDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HourglassEmpty,
                    contentDescription = "Empty Slot",
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Slot $slotNumber",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.3f)
                )
                Text(
                    text = "Waiting...",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.2f)
                )
            }
        }
    }
}
