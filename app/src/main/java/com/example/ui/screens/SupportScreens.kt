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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserStatsEntity
import com.example.ui.components.GameActionButton
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandIndigoDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.KidsCardPink
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SecondaryCoral
import com.example.ui.theme.SuccessGreen

@Composable
fun SettingsScreen(
    userStats: UserStatsEntity,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onToggleKidsMode: (Boolean) -> Unit,
    onNavigateParentalControl: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onNavigateAbout: () -> Unit,
    onBack: () -> Unit
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
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "SETTINGS",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Audio, Controls & Safety",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Audio & Haptics Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AUDIO & FEEDBACK",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        SettingToggleRow(
                            title = "Sound Effects",
                            icon = Icons.Default.VolumeUp,
                            isChecked = userStats.soundEnabled,
                            onCheckedChange = onToggleSound,
                            testTag = "toggle_sound"
                        )

                        SettingToggleRow(
                            title = "Background Music",
                            icon = Icons.Default.MusicNote,
                            isChecked = userStats.musicEnabled,
                            onCheckedChange = onToggleMusic,
                            testTag = "toggle_music"
                        )

                        SettingToggleRow(
                            title = "Haptic Vibration",
                            icon = Icons.Default.TouchApp,
                            isChecked = userStats.hapticEnabled,
                            onCheckedChange = onToggleHaptics,
                            testTag = "toggle_haptics"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Family & Kids Mode
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "FAMILY & KIDS SAFETY",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = KidsCardPink
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        SettingToggleRow(
                            title = "Kids Friendly Mode",
                            icon = Icons.Default.ChildCare,
                            isChecked = userStats.isKidsMode,
                            onCheckedChange = onToggleKidsMode,
                            testTag = "toggle_kids_mode"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateParentalControl() }
                                .testTag("parental_control_btn"),
                            shape = RoundedCornerShape(12.dp),
                            color = BrandCardDark
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = "Parental PIN Lock", style = MaterialTheme.typography.titleMedium, color = Color.White)
                                }
                                Text(
                                    text = if (userStats.isParentalLocked) "LOCKED" else "OFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (userStats.isParentalLocked) SuccessGreen else Color(0xFF90A4AE)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // System & Admin
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ADMIN & ABOUT",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF90A4AE)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateAdmin() }
                                .testTag("admin_panel_btn"),
                            shape = RoundedCornerShape(12.dp),
                            color = BrandCardDark
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = PrimaryCyan, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Admin Panel", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateAbout() }
                                .testTag("about_app_btn"),
                            shape = RoundedCornerShape(12.dp),
                            color = BrandCardDark
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Help, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "About & Credits", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GameActionButton(
                    text = "BACK TO MENU",
                    onClick = onBack,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "settings_back_btn"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryCyan,
                checkedTrackColor = PrimaryCyan.copy(alpha = 0.3f),
                uncheckedThumbColor = Color(0xFF90A4AE),
                uncheckedTrackColor = BrandCardDark
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
fun ParentalControlScreen(
    currentStats: UserStatsEntity,
    onSetPin: (pin: String, locked: Boolean) -> Unit,
    onBack: () -> Unit
) {
    var pinInput by remember { mutableStateOf("") }
    var isLocked by remember { mutableStateOf(currentStats.isParentalLocked) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                Icon(Icons.Default.Security, contentDescription = null, tint = AccentGold, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "PARENTAL CONTROL PIN",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Set a 4-digit PIN to restrict multiplayer matchmaking and in-game shop access for young kids",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pinInput = it },
                    label = { Text("Enter 4-Digit PIN") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center, letterSpacing = 8.sp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .testTag("parent_pin_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AccentGold,
                        unfocusedBorderColor = BrandBorderDark
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                SettingToggleRow(
                    title = "Enable Parental Lock",
                    icon = Icons.Default.Lock,
                    isChecked = isLocked,
                    onCheckedChange = { isLocked = it },
                    testTag = "toggle_parental_lock"
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                GameActionButton(
                    text = "SAVE PIN & SETTINGS",
                    onClick = { onSetPin(pinInput, isLocked) },
                    enabled = pinInput.length == 4 || !isLocked,
                    icon = Icons.Default.Check,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "save_pin_btn"
                )

                Spacer(modifier = Modifier.height(10.dp))

                GameActionButton(
                    text = "CANCEL",
                    onClick = onBack,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "cancel_pin_btn"
                )
            }
        }
    }
}

@Composable
fun TutorialScreen(
    onBack: () -> Unit
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
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "HOW TO PLAY",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Master the rules and become the arena champion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(20.dp))

                TutorialStepCard(
                    stepNumber = 1,
                    title = "Pick & Place Puzzle Pieces",
                    desc = "Tap any puzzle piece in the bottom carousel tray, then tap the corresponding board cell to place it. Pieces snap smoothly into place when correctly matched."
                )

                Spacer(modifier = Modifier.height(12.dp))

                TutorialStepCard(
                    stepNumber = 2,
                    title = "Speed & Accuracy Bonus",
                    desc = "Finish quickly and avoid wrong moves to maximize your match score. Faster puzzle solves give up to 500 extra speed bonus points."
                )

                Spacer(modifier = Modifier.height(12.dp))

                TutorialStepCard(
                    stepNumber = 3,
                    title = "Multiplayer Battle Mode",
                    desc = "Join quick matches or invite friends with a 6-digit room code. All players receive the exact same puzzle and scrambled piece seed. First player to complete wins!"
                )

                Spacer(modifier = Modifier.height(12.dp))

                TutorialStepCard(
                    stepNumber = 4,
                    title = "Use Smart Hints Wisely",
                    desc = "Need help? Tap the HINT button to highlight the target slot, turn on a ghost background guide, or auto-place a piece with coins."
                )

                Spacer(modifier = Modifier.height(20.dp))

                GameActionButton(
                    text = "GOT IT, LET'S PLAY!",
                    onClick = onBack,
                    icon = Icons.Default.AutoAwesome,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "tutorial_finish_btn"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TutorialStepCard(stepNumber: Int, title: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
        border = BorderStroke(1.dp, BrandBorderDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = PrimaryCyan.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "$stepNumber", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = PrimaryCyan)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF90A4AE))
            }
        }
    }
}

@Composable
fun AdminScreen(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "ADMIN CONTROLS", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color.White)
                Text(text = "System Diagnostics & Game Management", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF90A4AE))

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "SERVER & BACKEND STATUS", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Matchmaking Gateway", color = Color(0xFF90A4AE))
                            Text(text = "ONLINE (Healthy)", color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Anti-Cheat Engine", color = Color(0xFF90A4AE))
                            Text(text = "ACTIVE", color = PrimaryCyan, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "AdMob Monetization Engine", color = Color(0xFF90A4AE))
                            Text(text = "READY", color = AccentGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GameActionButton(
                    text = "BACK",
                    onClick = onBack,
                    gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "admin_back_btn"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
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
                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, PrimaryCyan, RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "Game Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "PICTURE PUZZLE BATTLE",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AccentGold
                ) {
                    Text(
                        text = "BY SOWROV SULTAN",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = BrandIndigoDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Version 1.0.0 (Production Release)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF90A4AE)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark),
                    border = BorderStroke(1.dp, BrandBorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "A thrilling multiplayer picture puzzle game designed for kids and adults. Challenge 2–4 players online, climb global leaderboards, unlock custom avatars and enjoy seamless real-time battles.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFB0BEC5),
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(text = "Privacy Policy: Safe for kids and family friendly.", style = MaterialTheme.typography.labelSmall, color = PrimaryCyan)
                        Text(text = "Support: support@picturepuzzlebattle.com", style = MaterialTheme.typography.labelSmall, color = PrimaryCyan)
                    }
                }
            }

            GameActionButton(
                text = "BACK TO MENU",
                onClick = onBack,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                modifier = Modifier.fillMaxWidth(),
                testTag = "about_back_btn"
            )
        }
    }
}
