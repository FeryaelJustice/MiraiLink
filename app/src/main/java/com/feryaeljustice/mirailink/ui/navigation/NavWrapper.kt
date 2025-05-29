package com.feryaeljustice.mirailink.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.feryaeljustice.mirailink.core.rememberInitializedStateFlow
import com.feryaeljustice.mirailink.ui.components.MiraiLinkBottomBar
import com.feryaeljustice.mirailink.ui.components.MiraiLinkTopBar
import com.feryaeljustice.mirailink.ui.screens.auth.AuthScreen
import com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel
import com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordScreen
import com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordViewModel
import com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationScreen
import com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationViewModel
import com.feryaeljustice.mirailink.ui.screens.chat.ChatScreen
import com.feryaeljustice.mirailink.ui.screens.chat.ChatViewModel
import com.feryaeljustice.mirailink.ui.screens.home.HomeScreen
import com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel
import com.feryaeljustice.mirailink.ui.screens.messages.MessagesScreen
import com.feryaeljustice.mirailink.ui.screens.messages.MessagesViewModel
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileScreen
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.SettingsScreen
import com.feryaeljustice.mirailink.ui.screens.settings.SettingsViewModel
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreen
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun NavWrapper(darkTheme: Boolean, onThemeChange: () -> Unit) {
    // Nav
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Session
    val sessionViewModel = hiltViewModel<GlobalSessionViewModel>()
    val sessionInitialized =
        rememberInitializedStateFlow(sessionViewModel.isGlobalSessionInitialized)

    // Session states
    val isAuthenticated by sessionViewModel.isAuthenticated.collectAsState(initial = false)
    val topBarConfig by sessionViewModel.topBarConfig.collectAsState()

    // Session events
    val onLogout = sessionViewModel.onLogout
    val needsToBeVerified = sessionViewModel.needsToBeVerified

    // Detectar logout y redirigir (para cuando hay una llamada que falla)
    LaunchedEffect(Unit) {
        onLogout.collect {
            navController.navigate(AppScreen.AuthScreen) {
                popUpTo(0) {
                    inclusive = true
                }
                // Limpia backstack
                launchSingleTop = true
            }
        }
    }

    // Detectar si el usuario necesita ser verificado
    LaunchedEffect(Unit) {
        needsToBeVerified.collect { userId ->
            navController.navigate(AppScreen.VerificationScreen(userId)) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            if (sessionInitialized && topBarConfig.showTopBar) {
                MiraiLinkTopBar(
                    darkTheme = darkTheme,
                    isAuthenticated = isAuthenticated,
                    onThemeChange = onThemeChange,
                    onNavigateHome = {
                        navController.navigate(AppScreen.HomeScreen) {
                            restoreState = true
                        }
                    },
                    onNavigateToSettings = {
                        navController.navigate(AppScreen.SettingsScreen)
                    },
                    showSettingsIcon = topBarConfig.showSettingsIcon,
                    title = topBarConfig.title,
                )
            }
        },
        bottomBar = {
            if (sessionInitialized && isAuthenticated && topBarConfig.showBottomBar) {
                MiraiLinkBottomBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }) { innerPadding ->
        if (!sessionInitialized) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            AppNavHost(
                navController = navController,
                sessionViewModel = sessionViewModel,
                isAuthenticated = isAuthenticated,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    sessionViewModel: GlobalSessionViewModel,
    isAuthenticated: Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.SplashScreen,
        modifier = modifier
    ) {
        composable<AppScreen.SplashScreen> {
            SplashScreen(isAuthenticated = isAuthenticated, onNavigateToHome = {
                navController.navigate(ScreensSubgraphs.Main) {
                    popUpTo(0) {
                        inclusive = false
                        saveState = false
                    }
                    restoreState = false
                    // 🔁 Limpia backstack
                    launchSingleTop = true
                }
            }, onNavigateToAuth = {
                navController.navigate(ScreensSubgraphs.Auth) {
                    launchSingleTop = true
                    popUpTo(AppScreen.SplashScreen) {
                        inclusive = true
                    }
                }
            })
        }
        authGraph(
            navController = navController,
            sessionViewModel = sessionViewModel,
            onLogin = {
                navController.navigate(ScreensSubgraphs.Main) {
                    launchSingleTop = true
                    popUpTo(AppScreen.AuthScreen) {
                        inclusive = true
                    }
                }
            },
            onRegister = {
                navController.navigate(ScreensSubgraphs.Main) {
                    launchSingleTop = true
                    popUpTo(AppScreen.AuthScreen) {
                        inclusive = true
                    }
                }
            }
        )
        appGraph(
            navController = navController,
            sessionViewModel = sessionViewModel,
        )
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    sessionViewModel: GlobalSessionViewModel,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
) {
    navigation<ScreensSubgraphs.Auth>(startDestination = AppScreen.AuthScreen) {
        composable<AppScreen.AuthScreen> {
            val authViewModel: AuthViewModel = hiltViewModel()
            AuthScreen(
                viewModel = authViewModel, sessionViewModel = sessionViewModel,
                onLogin = {
                    onLogin()
                }, onRegister = {
                    onRegister()
                }, onRequestPasswordReset = { email ->
                    navController.navigate(AppScreen.RecoverPasswordScreen(email = email))
                })
        }
        composable<AppScreen.RecoverPasswordScreen> { backStackEntry ->
            val recoverPasswordScreen: AppScreen.RecoverPasswordScreen = backStackEntry.toRoute()
            val recoverPasswordViewModel: RecoverPasswordViewModel = hiltViewModel()
            RecoverPasswordScreen(
                viewModel = recoverPasswordViewModel,
                sessionViewModel = sessionViewModel,
                email = recoverPasswordScreen.email,
                onConfirmedRecoverPassword = {
                    navController.popBackStack()
                }
            )
        }
    }
}

private fun NavGraphBuilder.appGraph(
    navController: NavHostController,
    sessionViewModel: GlobalSessionViewModel,
) {
    navigation<ScreensSubgraphs.Main>(startDestination = AppScreen.HomeScreen) {
        composable<AppScreen.HomeScreen> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                sessionViewModel = sessionViewModel
            )
        }

        composable<AppScreen.MessagesScreen> {
            val messagesViewModel: MessagesViewModel = hiltViewModel()
            MessagesScreen(
                viewModel = messagesViewModel,
                sessionViewModel = sessionViewModel,
                onNavigateToChat = { userId ->
                    navController.navigate(AppScreen.ChatScreen(userId = userId))
                })
        }

        composable<AppScreen.ChatScreen> { backStackEntry ->
            val chatScreen: AppScreen.ChatScreen = backStackEntry.toRoute()
            val chatViewModel: ChatViewModel = hiltViewModel()
            ChatScreen(
                viewModel = chatViewModel,
                sessionViewModel = sessionViewModel,
                userId = chatScreen.userId
            )
        }

        composable<AppScreen.ProfileScreen> {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(viewModel = profileViewModel, sessionViewModel = sessionViewModel)
        }

        composable<AppScreen.VerificationScreen> { backStackEntry ->
            val verificationScreen: AppScreen.VerificationScreen = backStackEntry.toRoute()
            val verificationViewModel: VerificationViewModel = hiltViewModel()
            VerificationScreen(
                viewModel = verificationViewModel,
                sessionViewModel = sessionViewModel,
                userId = verificationScreen.userId,
                onFinish = {
                    sessionViewModel.showBars()
                    navController.navigate(AppScreen.HomeScreen){
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<AppScreen.SettingsScreen> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                sessionViewModel = sessionViewModel,
                onLogout = {
                    navController.navigate(ScreensSubgraphs.Auth) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
        }
    }
}

/*
inline fun <reified T : Parcelable> createNavType(): NavType<T> {
    return object : NavType<T>(isNullableAllowed = true) {

        override fun get(bundle: SavedState, key: String): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, T::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString<T>(value)
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: SavedState, key: String, value: T) {
            bundle.putParcelable(key, value)
        }
    }
}
*/
