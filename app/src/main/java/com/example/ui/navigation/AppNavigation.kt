package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.model.Difficulty
import com.example.data.model.RoomState
import com.example.data.repository.PuzzleCatalog
import com.example.ui.components.MatchCountdownOverlay
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.ActivePuzzleGameScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CategorySelectionScreen
import com.example.ui.screens.DailyChallengeScreen
import com.example.ui.screens.DifficultySelectionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinRoomScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MultiplayerMenuScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ParentalControlScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RoomLobbyScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TutorialScreen
import com.example.ui.theme.BrandIndigoDark
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.MainViewModel

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val DIFFICULTIES = "difficulties"
    const val MULTIPLAYER_MENU = "multiplayer_menu"
    const val JOIN_ROOM = "join_room"
    const val ROOM_LOBBY = "room_lobby"
    const val ACTIVE_GAME = "active_game"
    const val LEADERBOARD = "leaderboard"
    const val ACHIEVEMENTS = "achievements"
    const val DAILY_CHALLENGE = "daily_challenge"
    const val PROFILE = "profile"
    const val SHOP = "shop"
    const val SETTINGS = "settings"
    const val PARENTAL_CONTROL = "parental_control"
    const val TUTORIAL = "tutorial"
    const val ADMIN = "admin"
    const val ABOUT = "about"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel(),
    gameViewModel: GameViewModel = viewModel()
) {
    val userStats by mainViewModel.userStats.collectAsState()
    val selectedCategory by mainViewModel.selectedCategory.collectAsState()
    val selectedDifficulty by mainViewModel.selectedDifficulty.collectAsState()
    val currentRoom by mainViewModel.currentRoom.collectAsState()
    val isDailyCompleted by mainViewModel.dailyCompletedToday.collectAsState()

    // Handle room state transitions (e.g. Host clicks Start -> countdown -> playing)
    LaunchedEffect(currentRoom?.state) {
        if (currentRoom?.state == RoomState.PLAYING) {
            currentRoom?.let { room ->
                gameViewModel.startMultiplayerMatch(room)
                navController.navigate(Routes.ACTIVE_GAME) {
                    popUpTo(Routes.ROOM_LOBBY) { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BrandIndigoDark
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Routes.SPLASH,
                enterTransition = { fadeIn(tween(250)) },
                exitTransition = { fadeOut(tween(250)) }
            ) {
                composable(Routes.SPLASH) {
                    SplashScreen(
                        onSplashFinished = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.ONBOARDING) {
                    OnboardingScreen(
                        onContinue = { isKids ->
                            mainViewModel.setKidsMode(isKids)
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.LOGIN) {
                    LoginScreen(
                        onGuestLogin = {
                            mainViewModel.authManager.loginAsGuest()
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        },
                        onGoogleLogin = {
                            mainViewModel.authManager.loginWithGoogle { success, _ ->
                                if (success) {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                    }
                                }
                            }
                        }
                    )
                }

                composable(Routes.HOME) {
                    HomeScreen(
                        userStats = userStats,
                        onNavigatePlay = { navController.navigate(Routes.CATEGORIES) },
                        onNavigateMultiplayer = { navController.navigate(Routes.MULTIPLAYER_MENU) },
                        onNavigatePractice = {
                            val puzzle = PuzzleCatalog.puzzles.first()
                            gameViewModel.startPracticeMatch(puzzle, Difficulty.EASY_3X3)
                            navController.navigate(Routes.ACTIVE_GAME)
                        },
                        onNavigateDailyChallenge = { navController.navigate(Routes.DAILY_CHALLENGE) },
                        onNavigateLeaderboard = { navController.navigate(Routes.LEADERBOARD) },
                        onNavigateAchievements = { navController.navigate(Routes.ACHIEVEMENTS) },
                        onNavigateShop = { navController.navigate(Routes.SHOP) },
                        onNavigateProfile = { navController.navigate(Routes.PROFILE) },
                        onNavigateSettings = { navController.navigate(Routes.SETTINGS) },
                        onNavigateTutorial = { navController.navigate(Routes.TUTORIAL) },
                        onNavigateAbout = { navController.navigate(Routes.ABOUT) }
                    )
                }

                composable(Routes.CATEGORIES) {
                    CategorySelectionScreen(
                        categories = PuzzleCatalog.categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { cat ->
                            mainViewModel.selectCategory(cat)
                            val puzzlesInCat = PuzzleCatalog.getPuzzlesForCategory(cat.id)
                            if (puzzlesInCat.isNotEmpty()) {
                                mainViewModel.selectPuzzle(puzzlesInCat.first())
                            }
                        },
                        onContinue = { navController.navigate(Routes.DIFFICULTIES) }
                    )
                }

                composable(Routes.DIFFICULTIES) {
                    DifficultySelectionScreen(
                        difficulties = Difficulty.entries,
                        selectedDifficulty = selectedDifficulty,
                        onDifficultySelected = { mainViewModel.selectDifficulty(it) },
                        onStartGame = {
                            val puzzle = mainViewModel.selectedPuzzle.value
                            gameViewModel.startPracticeMatch(puzzle, selectedDifficulty)
                            navController.navigate(Routes.ACTIVE_GAME)
                        }
                    )
                }

                composable(Routes.MULTIPLAYER_MENU) {
                    MultiplayerMenuScreen(
                        onQuickMatchSelected = { playerCount ->
                            mainViewModel.startQuickMatch(playerCount)
                            navController.navigate(Routes.ROOM_LOBBY)
                        },
                        onCreateRoomSelected = {
                            mainViewModel.createPrivateRoom()
                            navController.navigate(Routes.ROOM_LOBBY)
                        },
                        onJoinRoomSelected = { navController.navigate(Routes.JOIN_ROOM) },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.JOIN_ROOM) {
                    JoinRoomScreen(
                        onJoinSubmitted = { code ->
                            mainViewModel.joinPrivateRoom(code) { success, _ ->
                                if (success) {
                                    navController.navigate(Routes.ROOM_LOBBY) {
                                        popUpTo(Routes.JOIN_ROOM) { inclusive = true }
                                    }
                                }
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.ROOM_LOBBY) {
                    if (currentRoom != null) {
                        RoomLobbyScreen(
                            room = currentRoom!!,
                            currentUserId = userStats.userId,
                            onToggleReady = { mainViewModel.togglePlayerReady() },
                            onStartMatch = { mainViewModel.startCountdown() },
                            onLeaveRoom = {
                                mainViewModel.leaveRoom()
                                navController.popBackStack(Routes.HOME, inclusive = false)
                            }
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        }
                    }
                }

                composable(Routes.ACTIVE_GAME) {
                    ActivePuzzleGameScreen(
                        gameViewModel = gameViewModel,
                        onMatchFinished = { navController.popBackStack(Routes.HOME, false) },
                        onExitGame = { navController.popBackStack(Routes.HOME, false) }
                    )
                }

                composable(Routes.LEADERBOARD) {
                    LeaderboardScreen(
                        mainViewModel = mainViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.ACHIEVEMENTS) {
                    AchievementsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.DAILY_CHALLENGE) {
                    DailyChallengeScreen(
                        userStats = userStats,
                        isCompletedToday = isDailyCompleted,
                        onStartChallenge = {
                            val dailyPuz = PuzzleCatalog.getDailyPuzzle()
                            gameViewModel.startPracticeMatch(dailyPuz, dailyPuz.defaultDifficulty)
                            navController.navigate(Routes.ACTIVE_GAME)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.PROFILE) {
                    ProfileScreen(
                        userStats = userStats,
                        onSaveProfile = { name, avatar ->
                            mainViewModel.updateProfile(name, avatar)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.SHOP) {
                    ShopScreen(
                        userCoins = userStats.coins,
                        onBuyItem = { item ->
                            mainViewModel.buyShopItem(item) { /* Handled */ }
                        },
                        onWatchAd = { mainViewModel.watchRewardedAdForCoins() },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.SETTINGS) {
                    SettingsScreen(
                        userStats = userStats,
                        onToggleSound = { mainViewModel.toggleSound(it) },
                        onToggleMusic = { mainViewModel.toggleMusic(it) },
                        onToggleHaptics = { mainViewModel.toggleHaptic(it) },
                        onToggleKidsMode = { mainViewModel.setKidsMode(it) },
                        onNavigateParentalControl = { navController.navigate(Routes.PARENTAL_CONTROL) },
                        onNavigateAdmin = { navController.navigate(Routes.ADMIN) },
                        onNavigateAbout = { navController.navigate(Routes.ABOUT) },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.PARENTAL_CONTROL) {
                    ParentalControlScreen(
                        currentStats = userStats,
                        onSetPin = { pin, locked ->
                            mainViewModel.setParentalLock(locked, pin)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.TUTORIAL) {
                    TutorialScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.ADMIN) {
                    AdminScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.ABOUT) {
                    AboutScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // Global Match Starting Countdown Overlay (When room state is STARTING)
            if (currentRoom?.state == RoomState.STARTING) {
                MatchCountdownOverlay(secondsRemaining = 3)
            }
        }
    }
}
