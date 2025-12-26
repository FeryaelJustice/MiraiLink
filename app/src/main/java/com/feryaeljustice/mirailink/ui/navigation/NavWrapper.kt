package com.feryaeljustice.mirailink.ui.navigation

import android.content.ClipData
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.constants.deepLinkBaseUrl
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkPrefs
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.bottombars.MiraiLinkBottomBar
import com.feryaeljustice.mirailink.ui.components.topbars.MiraiLinkTopBar
import com.feryaeljustice.mirailink.ui.components.topbars.TopBarLayoutDirection
import com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatScreen
import com.feryaeljustice.mirailink.ui.screens.auth.AuthScreen
import com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordScreen
import com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationScreen
import com.feryaeljustice.mirailink.ui.screens.chat.ChatScreen
import com.feryaeljustice.mirailink.ui.screens.home.HomeScreen
import com.feryaeljustice.mirailink.ui.screens.messages.MessagesScreen
import com.feryaeljustice.mirailink.ui.screens.onboarding.OnboardingScreen
import com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureScreen
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileScreen
import com.feryaeljustice.mirailink.ui.screens.settings.SettingsScreen
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackScreen
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorScreen
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreen
import com.feryaeljustice.mirailink.ui.utils.composition.LocalShowSnackbar
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Suppress("ktlint:standard:function-naming", "EffectKeys")
@Composable
fun NavWrapper(
    darkTheme: Boolean,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier,
    navAnalyticsVm: NavAnalyticsViewModel = koinViewModel(),
) {
    // Utils
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current

    // Entry points
    val miraiLinkPrefs: GlobalMiraiLinkPrefs = koinInject()
    val miraiLinkSession: GlobalMiraiLinkSession = koinInject()

    // Session states
//    val isAppSessionInitialized by miraiLinkSession.isInitialized.collectAsStateWithLifecycle()
    val isAuthenticated by miraiLinkSession.isAuthenticated.collectAsStateWithLifecycle()
    val topBarConfig by miraiLinkSession.topBarConfig.collectAsStateWithLifecycle()
    val currentUserId by miraiLinkSession.currentUserId.collectAsStateWithLifecycle()
    val hasProfilePicture by miraiLinkSession.hasProfilePicture.collectAsStateWithLifecycle()
    val isVerified by miraiLinkSession.isVerified.collectAsStateWithLifecycle()

    // Session events
    val onLogout = miraiLinkSession.onLogout

    // Handlers
    val copiedToClipboardTxt = stringResource(R.string.copied_to_clipboard)

    val showSnackbar: (String) -> Unit = { msg ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message = msg)
        }
    }

    val copyToClipboard: (String) -> Unit = { msg ->
        scope.launch {
            clipboard.setClipEntry(
                ClipData.newPlainText(msg, msg).toClipEntry(),
            )
            showToast(context, copiedToClipboardTxt, Toast.LENGTH_SHORT)
        }
    }

    // Nav3: top-level stacks de la app
    val topLevelRoutes: Set<NavKey> =
        remember {
            setOf(
                AppScreen.SplashScreen,
                ScreensSubgraphs.Auth,
                ScreensSubgraphs.Main,
            )
        }

    val navigationState =
        rememberNavigationState(
            startRoute = AppScreen.SplashScreen,
            topLevelRoutes = topLevelRoutes,
        )

    val navigator = remember { Navigator(navigationState) }

    // Current key (equivalente a currentDestination)
    val currentKey = navigationState.currentKey()

    // 1. Reacción a logout
    LaunchedEffect(Unit) {
        /*  snapshotFlow { isAppSessionInitialized }
              .filter { it }
              .take(1)
              .collect {*/
        onLogout.collect {
            if (miraiLinkPrefs.isOnboardingCompleted()) {
                navigator.resetToTopLevel(
                    topLevel = ScreensSubgraphs.Auth,
                    firstChild = AppScreen.AuthScreen,
                )
            } else {
                navigator.resetToTopLevel(
                    topLevel = AppScreen.SplashScreen,
                    firstChild = AppScreen.SplashScreen,
                )
            }
        }
    }

    // 2. Requiere verificación o chequeo de foto de perfil
    LaunchedEffect(isAuthenticated, currentUserId, isVerified, hasProfilePicture) {
        if (!isAuthenticated) return@LaunchedEffect

        val userId = currentUserId ?: return@LaunchedEffect

        if (!isVerified) {
            navigator.resetToTopLevel(
                topLevel = ScreensSubgraphs.Main,
                firstChild = AppScreen.VerificationScreen(userId),
            )
        } else if (hasProfilePicture == false) {
            navigator.resetToTopLevel(
                topLevel = ScreensSubgraphs.Main,
                firstChild = AppScreen.ProfilePictureScreen,
            )
        }
    }

    // Analytics: log “ruta” estable por key
    LaunchedEffect(currentKey) {
        navAnalyticsVm.logScreen(currentKey.debugRouteName())
    }

    // EntryProvider Nav3 (esto reemplaza a tu NavHost + graphs)
    val entries =
        remember(navigator) {
            entryProvider {
                // Top level containers (si caes aquí, rediriges a su start real)
                entry<AppScreen.SplashScreen> {
                    SplashScreen(
                        miraiLinkSession = miraiLinkSession,
                        onInitialNavigation = { action ->
                            when (action) {
                                InitialNavigationAction.GoToAuth -> {
                                    navigator.resetToTopLevel(
                                        ScreensSubgraphs.Auth,
                                        AppScreen.AuthScreen,
                                    )
                                }

                                InitialNavigationAction.GoToHome -> {
                                    navigator.resetToTopLevel(
                                        ScreensSubgraphs.Main,
                                        AppScreen.HomeScreen,
                                    )
                                }

                                InitialNavigationAction.GoToOnboarding -> {
                                    navigator.resetToTopLevel(
                                        AppScreen.SplashScreen,
                                        AppScreen.OnboardingScreen,
                                    )
                                }
                            }
                        },
                    )
                }

                entry<ScreensSubgraphs.Auth> {
                    // Si alguien te navega a Auth “base”, empuja AuthScreen
                    LaunchedEffect(Unit) { navigator.navigate(AppScreen.AuthScreen) }
                }

                entry<ScreensSubgraphs.Main> {
                    // Si alguien te navega a Main “base”, empuja HomeScreen
                    LaunchedEffect(Unit) { navigator.navigate(AppScreen.HomeScreen) }
                }

                // Onboarding
                entry<AppScreen.OnboardingScreen> {
                    OnboardingScreen(
                        onFinish = {
                            miraiLinkPrefs.markOnboardingCompleted()
                            val destination =
                                if (isAuthenticated) ScreensSubgraphs.Main else ScreensSubgraphs.Auth
                            if (destination == ScreensSubgraphs.Main) {
                                navigator.resetToTopLevel(
                                    ScreensSubgraphs.Main,
                                    AppScreen.HomeScreen,
                                )
                            } else {
                                navigator.resetToTopLevel(
                                    ScreensSubgraphs.Auth,
                                    AppScreen.AuthScreen,
                                )
                            }
                        },
                    )
                }

                // Auth flow
                entry<AppScreen.AuthScreen> {
                    AuthScreen(
                        miraiLinkSession = miraiLinkSession,
                        onLogin = {
                            navigator.resetToTopLevel(
                                ScreensSubgraphs.Main,
                                AppScreen.HomeScreen,
                            )

                            miraiLinkSession.showBars()
                            miraiLinkSession.enableBars()
                            miraiLinkSession.showTopBarSettingsIcon()
                        },
                        onRegister = {
                            navigator.resetToTopLevel(
                                ScreensSubgraphs.Main,
                                AppScreen.HomeScreen,
                            )

                            miraiLinkSession.showBars()
                            miraiLinkSession.enableBars()
                            miraiLinkSession.showTopBarSettingsIcon()
                        },
                        onRequestPasswordReset = { email ->
                            navigator.navigate(AppScreen.RecoverPasswordScreen(email))

                            miraiLinkSession.showBars()
                            miraiLinkSession.enableBars()
                            miraiLinkSession.showTopBarSettingsIcon()
                        },
                    )
                }

                entry<AppScreen.RecoverPasswordScreen> { key ->
                    RecoverPasswordScreen(
                        miraiLinkSession = miraiLinkSession,
                        email = key.email,
                        onConfirmedRecoverPassword = {
                            // Vuelves a Auth
                            navigator.resetToTopLevel(ScreensSubgraphs.Auth, AppScreen.AuthScreen)
                        },
                    )
                }

                // Main flow
                entry<AppScreen.ProfilePictureScreen> {
                    ProfilePictureScreen(
                        miraiLinkSession = miraiLinkSession,
                        onProfileUpload = {
                            navigator.resetToTopLevel(ScreensSubgraphs.Main, AppScreen.HomeScreen)
                        },
                    )
                }

                entry<AppScreen.HomeScreen> {
                    // Deep link tracking (si quieres)
                    LaunchedEffect(Unit) {
                        navAnalyticsVm.logDeepLink(deepLinkBaseUrl)
                    }

                    HomeScreen(miraiLinkSession = miraiLinkSession)
                }

                entry<AppScreen.MessagesScreen> {
                    MessagesScreen(
                        miraiLinkSession = miraiLinkSession,
                        onNavigateToChat = { userId ->
                            navigator.navigate(AppScreen.ChatScreen(userId))
                        },
                        onNavigateToAiChat = {
                            navigator.navigate(AppScreen.AiChatScreen)
                        },
                    )
                }

                entry<AppScreen.ChatScreen> { key ->
                    ChatScreen(
                        miraiLinkSession = miraiLinkSession,
                        userId = key.userId,
                        onBackClick = { navigator.goBack() },
                    )
                }

                entry<AppScreen.AiChatScreen> { _ ->
                    AiChatScreen(
                        miraiLinkSession = miraiLinkSession,
                    )
                }

                entry<AppScreen.ProfileScreen> {
                    ProfileScreen(miraiLinkSession = miraiLinkSession)
                }

                entry<AppScreen.VerificationScreen> { key ->
                    VerificationScreen(
                        miraiLinkSession = miraiLinkSession,
                        userId = key.userId,
                        onFinish = {
                            navigator.resetToTopLevel(ScreensSubgraphs.Main, AppScreen.HomeScreen)
                        },
                    )
                }

                entry<AppScreen.SettingsScreen> {
                    SettingsScreen(
                        miraiLinkSession = miraiLinkSession,
                        goToFeedbackScreen = { navigator.navigate(AppScreen.FeedbackScreen) },
                        goToConfigureTwoFactorScreen = { navigator.navigate(AppScreen.ConfigureTwoFactorScreen) },
                        showToast = { msg, duration -> showToast(context, msg, duration) },
                        copyToClipBoard = copyToClipboard,
                    )
                }

                entry<AppScreen.FeedbackScreen> {
                    FeedbackScreen(
                        showToast = { msg, duration -> showToast(context, msg, duration) },
                        onBackClick = { navigator.goBack() },
                    )
                }

                entry<AppScreen.ConfigureTwoFactorScreen> {
                    ConfigureTwoFactorScreen(
                        miraiLinkSession = miraiLinkSession,
                        onBackClick = { navigator.goBack() },
                        onShowError = { error -> if (error.isNotBlank()) showSnackbar(error) },
                    )
                }
            }
        }

    // UI
    Box(modifier = modifier) {
        CompositionLocalProvider(LocalShowSnackbar provides showSnackbar) {
            Scaffold(
                topBar = {
                    if (topBarConfig.showTopBar) {
                        val isAuthUi =
                            navigationState.topLevelRoute == ScreensSubgraphs.Auth || currentKey is AppScreen.AuthScreen
                        MiraiLinkTopBar(
                            darkTheme = darkTheme,
                            enabled = !topBarConfig.disableTopBar && isAuthenticated,
                            isAuthenticated = isAuthenticated,
                            showSettingsIcon = topBarConfig.showSettingsIcon,
                            title = topBarConfig.title,
                            onThemeChange = onThemeChange,
                            layoutDirection = if (isAuthUi) TopBarLayoutDirection.COLUMN else TopBarLayoutDirection.ROW,
                            onNavigateHome = {
                                if (currentKey !is AppScreen.HomeScreen) {
                                    navigator.resetToTopLevel(
                                        ScreensSubgraphs.Main,
                                        AppScreen.HomeScreen,
                                    )
                                }
                            },
                            onNavigateToSettings = {
                                if (currentKey !is AppScreen.SettingsScreen) {
                                    navigator.navigate(AppScreen.SettingsScreen)
                                }
                            },
                        )
                    }
                },
                bottomBar = {
                    if (topBarConfig.showBottomBar) {
                        // Solo tiene sentido mostrarlo en Main (y cuando no estén forzadas barras off)
                        val showBottom = navigationState.topLevelRoute == ScreensSubgraphs.Main
                        if (showBottom) {
                            MiraiLinkBottomBar(
                                navigator = navigator,
                                navState = navigationState,
                                enabled = !topBarConfig.disableBottomBar,
                                onDestinationClick = {
                                    if (topBarConfig.disableBottomBar) showSnackbar("Bottom bar is disabled")
                                },
                            )
                        }
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            ) { innerPadding ->
                NavDisplay(
                    entries = navigationState.toEntries(entries),
                    onBack = { navigator.goBack() },
                    // Si algún día pones destinos dialog: añade metadata + esta strategy
                    sceneStrategy = remember { DialogSceneStrategy() },
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(250),
                        ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(250),
                            )
                    },
                    popTransitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(250),
                        ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(250),
                            )
                    },
                    predictivePopTransitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(250),
                        ) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(250),
                            )
                    },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

/* ---------------------------
   Helpers
   --------------------------- */

private fun NavKey.debugRouteName(): String =
    when (this) {
        is ScreensSubgraphs.Auth -> "sg_auth"
        is ScreensSubgraphs.Main -> "sg_main"
        is AppScreen.SplashScreen -> "splash"
        is AppScreen.OnboardingScreen -> "onboarding"
        is AppScreen.AuthScreen -> "auth"
        is AppScreen.RecoverPasswordScreen -> "recover_password"
        is AppScreen.VerificationScreen -> "verification"
        is AppScreen.ProfilePictureScreen -> "profile_picture"
        is AppScreen.HomeScreen -> "home"
        is AppScreen.MessagesScreen -> "messages"
        is AppScreen.ChatScreen -> "chat"
        is AppScreen.AiChatScreen -> "ai_chat"
        is AppScreen.SettingsScreen -> "settings"
        is AppScreen.ProfileScreen -> "profile"
        is AppScreen.FeedbackScreen -> "feedback"
        is AppScreen.ConfigureTwoFactorScreen -> "configure_2fa"
        else -> this::class.simpleName ?: "unknown"
    }
