package com.feryaeljustice.mirailink.ui.navigation

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.constants.deepLinkBaseUrl
import com.feryaeljustice.mirailink.ui.components.bottombars.MiraiLinkBottomBar
import com.feryaeljustice.mirailink.ui.components.topbars.MiraiLinkTopBar
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
import com.feryaeljustice.mirailink.ui.screens.onboarding.OnboardingScreen
import com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureScreen
import com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureViewModel
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileScreen
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.SettingsScreen
import com.feryaeljustice.mirailink.ui.screens.settings.SettingsViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackScreen
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorScreen
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorViewModel
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreen
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreenViewModel
import com.feryaeljustice.mirailink.ui.state.GlobalMiraiLinkPrefsViewModel
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel
import com.feryaeljustice.mirailink.ui.utils.composition.LocalShowSnackbar
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import kotlinx.coroutines.launch

@Composable
fun NavWrapper(darkTheme: Boolean, onThemeChange: () -> Unit) {
    // Nav
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Utils
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current
    val context = LocalContext.current

    val showSnackbar: (String) -> Unit = { msg ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message = msg)
        }
    }
    val copyToClipBoard: (String) -> Unit = { msg ->
        scope.launch {
            clipboard.setClipEntry(
                ClipData.newPlainText(
                    msg,
                    msg
                ).toClipEntry()
            )
            showToast(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
        }
    }

    // VMs globales
    val miraiLinkPrefs = hiltViewModel<GlobalMiraiLinkPrefsViewModel>()
    val sessionViewModel = hiltViewModel<GlobalSessionViewModel>()

    // Nav
    val currentDestination = navBackStackEntry?.destination

    // Session states
//    val isAppSessionInitialized by sessionViewModel.isInitialized.collectAsState()
    val isAuthenticated by sessionViewModel.isAuthenticated.collectAsState()
    val topBarConfig by sessionViewModel.topBarConfig.collectAsState()
    val currentUserId by sessionViewModel.currentUserId.collectAsState()
    val hasProfilePicture by sessionViewModel.hasProfilePicture.collectAsState()
    val isVerified by sessionViewModel.isVerified.collectAsState()

    // Session events
    val onLogout = sessionViewModel.onLogout

    // 1. Reacción a logout
    LaunchedEffect(Unit) {
        /*  snapshotFlow { isAppSessionInitialized }
              .filter { it }
              .take(1)
              .collect {*/
        onLogout.collect {
            if (miraiLinkPrefs.isOnboardingCompleted()) {
                navController.navigate(AppScreen.AuthScreen) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
//            }
    }

    // 2. Requiere verificación o chequeo de foto de perfil
    LaunchedEffect(isAuthenticated, currentUserId, isVerified, hasProfilePicture) {
        if (isAuthenticated) {
            currentUserId?.let { userId ->
                if (!isVerified) {
                    navController.navigate(AppScreen.VerificationScreen(userId)) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (hasProfilePicture == false) {
                    navController.navigate(AppScreen.ProfilePictureScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    CompositionLocalProvider(LocalShowSnackbar provides showSnackbar) {
        Scaffold(
            topBar = {
                if (topBarConfig.showTopBar) {
                    MiraiLinkTopBar(
                        darkTheme = darkTheme,
                        enabled = !topBarConfig.disableTopBar,
                        isAuthenticated = isAuthenticated,
                        showSettingsIcon = topBarConfig.showSettingsIcon,
                        title = topBarConfig.title,
                        onThemeChange = onThemeChange,
                        onNavigateHome = {
                            val isHomeRoute =
                                navBackStackEntry?.destination?.hasRoute(AppScreen.HomeScreen::class)
                            if (isHomeRoute == false)
                                navController.navigate(AppScreen.HomeScreen) {
                                    popUpTo(AppScreen.HomeScreen) {
                                        inclusive = true // elimina duplicado si ya existía
                                    }
                                    launchSingleTop =
                                        true // evita nueva instancia si ya está en el top
                                    restoreState = true // restaura el scroll/estado si aplica
                                }
                        },
                        onNavigateToSettings = {
                            val isSettingsRoute =
                                navBackStackEntry?.destination?.hasRoute(AppScreen.SettingsScreen::class)
                            if (isSettingsRoute == false) {
                                navController.navigate(AppScreen.SettingsScreen)
                            }
                        },
                    )
                }
            },
            bottomBar = {
                if (topBarConfig.showBottomBar) {
                    MiraiLinkBottomBar(
                        navController = navController,
                        currentDestination = currentDestination,
                        enabled = !topBarConfig.disableBottomBar,
                        onDestinationClick = {
                            if (topBarConfig.disableBottomBar) {
                                showSnackbar("Bottom bar is disabled")
                            }
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                sessionViewModel = sessionViewModel,
                miraiLinkPrefs = miraiLinkPrefs,
                modifier = Modifier.padding(innerPadding),
                showSnackbar = showSnackbar,
                copyToClipboard = copyToClipBoard,
                isAuthenticated = isAuthenticated,
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    sessionViewModel: GlobalSessionViewModel,
    miraiLinkPrefs: GlobalMiraiLinkPrefsViewModel,
    modifier: Modifier = Modifier,
    showSnackbar: (String) -> Unit = {},
    copyToClipboard: (String) -> Unit = {},
    isAuthenticated: Boolean,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    NavHost(
        navController = navController,
        startDestination = AppScreen.SplashScreen,
        modifier = modifier
    ) {
        composable<AppScreen.SplashScreen> {
            val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
            SplashScreen(
                viewModel = splashScreenViewModel,
                sessionViewModel = sessionViewModel,
                onInitialNavigation = { action ->
                    when (action) {
                        is InitialNavigationAction.GoToAuth -> {
                            navController.navigate(ScreensSubgraphs.Auth) {
                                popUpTo(AppScreen.SplashScreen) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }

                        is InitialNavigationAction.GoToHome -> {
                            navController.navigate(ScreensSubgraphs.Main) {
                                popUpTo(0) {
                                    inclusive = false
                                    saveState = false
                                }
                                restoreState = false
                                launchSingleTop = true
                            }
                        }

                        is InitialNavigationAction.GoToOnboarding -> {
                            navController.navigate(AppScreen.OnboardingScreen) {
                                popUpTo(AppScreen.SplashScreen) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                },
            )
        }
        composable<AppScreen.OnboardingScreen> {
            OnboardingScreen(onFinish = {
                // 1. Guardar flag en DataStore
                miraiLinkPrefs.markOnboardingCompleted()

                // 2. Navegación condicional
                val destination =
                    if (isAuthenticated) ScreensSubgraphs.Main else ScreensSubgraphs.Auth

                navController.navigate(destination) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
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
            context = context,
            showSnackbar = showSnackbar,
            copyToClipBoard = copyToClipboard,
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
                onLogin = { userId ->
                    onLogin()
                }, onRegister = { userId ->
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
                    navController.navigate(AppScreen.AuthScreen) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.appGraph(
    navController: NavHostController,
    sessionViewModel: GlobalSessionViewModel,
    context: Context,
    showSnackbar: (String) -> Unit,
    copyToClipBoard: (String) -> Unit,
) {
    navigation<ScreensSubgraphs.Main>(startDestination = AppScreen.HomeScreen) {
        composable<AppScreen.ProfilePictureScreen> {
            val profilePictureViewModel: ProfilePictureViewModel = hiltViewModel()
            ProfilePictureScreen(
                viewModel = profilePictureViewModel,
                sessionViewModel = sessionViewModel,
                onProfileUploaded = {
                    navController.navigate(AppScreen.HomeScreen) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
            )
        }

        composable<AppScreen.HomeScreen>(deepLinks = listOf(navDeepLink {
            uriPattern = deepLinkBaseUrl
        })) {
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
                userId = chatScreen.userId,
                onBackClick = {
                    navController.navigateUp()
                }
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
                    navController.navigate(AppScreen.HomeScreen) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<AppScreen.SettingsScreen> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                sessionViewModel = sessionViewModel,
                goToFeedbackScreen = {
                    navController.navigate(AppScreen.FeedbackScreen)
                },
                goToConfigureTwoFactorScreen = {
                    navController.navigate(AppScreen.ConfigureTwoFactorScreen)
                },
                showToast = { msg, duration ->
                    showToast(context = context, message = msg, duration = duration)
                },
                copyToClipBoard = copyToClipBoard
            )
        }

        composable<AppScreen.FeedbackScreen> {
            val feedbackViewModel: FeedbackViewModel = hiltViewModel()
            FeedbackScreen(
                viewModel = feedbackViewModel,
                showToast = { msg, duration ->
                    showToast(context = context, message = msg, duration = duration)
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<AppScreen.ConfigureTwoFactorScreen> {
            val configureTwoFactorViewModel: ConfigureTwoFactorViewModel = hiltViewModel()
            ConfigureTwoFactorScreen(
                viewModel = configureTwoFactorViewModel,
                sessionViewModel = sessionViewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onShowError = { error -> if (error.isNotBlank()) showSnackbar(error) }
            )
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
