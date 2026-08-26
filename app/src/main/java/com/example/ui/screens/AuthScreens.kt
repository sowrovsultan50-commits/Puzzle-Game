package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AvatarIcon
import com.example.ui.components.GameActionButton
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandIndigoDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.KidsCardPink
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SecondaryCoral
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.4f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(700, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(200)
        )
        alpha.animateTo(1.0f, tween(400))
        delay(1400)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BrandIndigoDark, Color(0xFF0F1B3E), BrandSurfaceDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Emblem
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(scale.value)
                    .clip(RoundedCornerShape(32.dp))
                    .border(3.dp, PrimaryCyan, RoundedCornerShape(32.dp))
                    .shadow(16.dp, RoundedCornerShape(32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon),
                    contentDescription = "Game Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PICTURE PUZZLE BATTLE",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Puzzle • Compete • Win",
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryCyan,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BrandCardDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorderDark)
            ) {
                Text(
                    text = "BY SOWROV SULTAN",
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentGold,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinue: (isKidsMode: Boolean) -> Unit
) {
    var selectedAgeGroup by remember { mutableStateOf<Boolean?>(null) } // true: Kids, false: Adult/General
    var selectedLanguage by remember { mutableStateOf("English") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandIndigoDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "WELCOME TO",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryCyan,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Picture Puzzle Battle",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Choose your personalized experience",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB0BEC5)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Kids Mode Card Choice
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (selectedAgeGroup == true) 2.5.dp else 1.dp,
                            color = if (selectedAgeGroup == true) AccentGold else BrandBorderDark,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedAgeGroup = true }
                        .testTag("choose_kids_mode"),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = KidsCardPink.copy(alpha = 0.2f),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ChildCare,
                                    contentDescription = null,
                                    tint = KidsCardPink,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Kids Mode",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Simple UI, friendly puzzles (4-16 pieces), parental safety",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF90A4AE)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Adult / Pro Mode Card Choice
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (selectedAgeGroup == false) 2.5.dp else 1.dp,
                            color = if (selectedAgeGroup == false) PrimaryCyan else BrandBorderDark,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedAgeGroup = false }
                        .testTag("choose_pro_mode"),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryCyan.copy(alpha = 0.2f),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = null,
                                    tint = PrimaryCyan,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Adult & Pro Mode",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Challenging 16-64 piece grids, speed timers, competitive ranked battles",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF90A4AE)
                            )
                        }
                    }
                }
            }

            // Bottom Continue Button
            GameActionButton(
                text = "CONTINUE",
                onClick = {
                    onContinue(selectedAgeGroup ?: false)
                },
                enabled = selectedAgeGroup != null,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth(),
                testTag = "onboarding_continue_btn"
            )
        }
    }
}

@Composable
fun LoginScreen(
    onGuestLogin: () -> Unit,
    onGoogleLogin: () -> Unit
) {
    var customGuestName by remember { mutableStateOf("PuzzlePlayer") }
    var selectedAvatar by remember { mutableStateOf("avatar_1") }

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
                text = "Player Profile Setup",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose your game avatar and player name",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF90A4AE)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Avatar Carousel
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("avatar_1", "avatar_2", "avatar_3", "avatar_4", "avatar_5", "avatar_6").forEach { avId ->
                    val isSelected = selectedAvatar == avId
                    Box(
                        modifier = Modifier
                            .scale(if (isSelected) 1.15f else 1.0f)
                            .clickable { selectedAvatar = avId }
                            .testTag("select_avatar_$avId")
                    ) {
                        AvatarIcon(avatarId = avId, size = 48.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = customGuestName,
                onValueChange = { if (it.length <= 15) customGuestName = it },
                label = { Text("Player Nickname") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nickname_input"),
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

            Spacer(modifier = Modifier.height(32.dp))

            GameActionButton(
                text = "PLAY AS GUEST",
                onClick = onGuestLogin,
                gradientColors = listOf(PrimaryCyan, Color(0xFF0099FF)),
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth(),
                testTag = "guest_login_btn"
            )

            Spacer(modifier = Modifier.height(14.dp))

            GameActionButton(
                text = "SIGN IN WITH GOOGLE",
                onClick = onGoogleLogin,
                gradientColors = listOf(BrandCardDark, BrandSurfaceDark),
                textColor = Color.White,
                icon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BrandBorderDark, RoundedCornerShape(18.dp)),
                testTag = "google_login_btn"
            )
        }
    }
}
