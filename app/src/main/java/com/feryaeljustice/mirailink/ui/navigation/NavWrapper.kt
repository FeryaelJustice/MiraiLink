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
        onLogout.collect {
            miraiLinkSession.hideBars()
            miraiLinkSession.disableBars()
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

    // 2. Control Centralizado de Sesión (Login / Verificación / ProfilePic / Home)
    // Este efecto es la única fuente de verdad para transicionar a "Main" cuando hay sesión.
    LaunchedEffect(isAuthenticated, currentUserId, isVerified, hasProfilePicture) {
        if (isAuthenticated) {
            val userId = currentUserId ?: return@LaunchedEffect
            
            // Determinar destino correcto según estado del usuario
            val (targetTopLevel, targetFirstChild) = when {
                !isVerified -> ScreensSubgraphs.Main to AppScreen.VerificationScreen(userId)
                hasProfilePicture == false -> ScreensSubgraphs.Main to AppScreen.ProfilePictureScreen
                else -> ScreensSubgraphs.Main to AppScreen.HomeScreen
            }

            // Lógica para decidir si navegar:
            // 1. Si NO estamos en Main (estamos en Auth o Splash) -> Navegar
            // 2. Si estamos en Main pero deberíamos estar en Verification/ProfilePic -> Navegar
            val currentTopLevel = navigationState.topLevelRoute
            val needsNavigation = currentTopLevel != ScreensSubgraphs.Main ||
                    (targetFirstChild !is AppScreen.HomeScreen && navigator.state.backStacks[ScreensSubgraphs.Main]?.lastOrNull() != targetFirstChild)

            if (needsNavigation) {
                // Configurar UI Bars para sesión activa
                miraiLinkSession.showBars()
                miraiLinkSession.enableBars()
                miraiLinkSession.showTopBarSettingsIcon()

                // Navegar
                navigator.resetToTopLevel(
                    topLevel = targetTopLevel,
                    firstChild = targetFirstChild,
                )
            }
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
                                    // Dejar que el LaunchedEffect de sesión maneje la entrada a Home
                                    // Pero para Splash explícito (autologin), el estado ya es true.
                                    // El LaunchedEffect se ejecutará al componer NavWrapper.
                                    // Aun así, para evitar parpadeo en splash, podemos dejar esto o confiar en el State.
                                    // Confiaremos en el State, pero el Splash podría quedar colgado si no cambiamos TopLevel.
                                    // Mejor: Si Splash dice GoToHome, es por autologin. Estado isAuthenticated ya es true?
                                    // Sí. Entonces el LaunchedEffect lo captará.
                                    // Podemos dejar vacío o un pequeño safety.
                                    // Para seguridad, forzamos aquí también si el efecto tardase, pero idealmente el efecto gana.
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
                    LaunchedEffect(Unit) { 
                         // Asegurar bares por si acaso se entra directo sin pasar por Auth
                        miraiLinkSession.showBars()
                        miraiLinkSession.enableBars()
                        miraiLinkSession.showTopBarSettingsIcon()
                        navigator.navigate(AppScreen.HomeScreen) 
                    }
                }

                // Onboarding
                entry<AppScreen.OnboardingScreen> {
                    OnboardingScreen(
                        onFinish = {
                            miraiLinkPrefs.markOnboardingCompleted()
                            // Auth/Main decisión se delega al estado (si isAuthenticated -> LaunchedEffect actuará)
                            // Si no está autenticado, vamos a Auth.
                            if (!isAuthenticated) {
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
                            // Delegado al LaunchedEffect centralizado
                        },
                        onRegister = {
                           // Delegado al LaunchedEffect centralizado
                        },
                        onRequestPasswordReset = { email ->
                            // Este es navegación interna de Auth, sí navega directo
                            miraiLinkSession.showBars() // Quizás no mostrar barras aquí?
                            miraiLinkSession.enableBars()
                            miraiLinkSession.showTopBarSettingsIcon()
                            navigator.navigate(AppScreen.RecoverPasswordScreen(email))
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
    /**
     * UI: Box que envuelve toda la app
     */
    Box(modifier = modifier) {
        /**
         * Compose Shared Element Transitions with Navigation 3:
         * https://developer.android.com/develop/ui/compose/animation/shared-elements
         */
        //SharedTransitionLayout {
        //    CompositionLocalProvider(
        //        LocalSharedTransitionScope provides this,
        //    ) {
        CompositionLocalProvider(LocalShowSnackbar provides showSnackbar) {
            /**
             * Scaffold general de la app
             */
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
                /**
                 * Navigation 3 root, nav display
                 */
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
    //    }
    //}
}

/**
 * Helpers
 */

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
