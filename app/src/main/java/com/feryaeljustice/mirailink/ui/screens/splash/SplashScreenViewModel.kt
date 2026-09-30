package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import com.feryaeljustice.mirailink.data.mappers.ui.toVersionCheckResultViewEntry
import com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import com.feryaeljustice.mirailink.ui.viewentries.VersionCheckResultViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SplashScreenViewModel(
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
    private val autologinUseCase: AutologinUseCase,
    private val checkOnboardingIsCompletedUseCase: CheckOnboardingIsCompleted,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
    private val store: FeatureFlagStore,
    private val isInChristmasMode: Boolean,
    private val miraiLinkSession: GlobalMiraiLinkSession,
) : ViewModel() {
    private val _updateDiagInfo = MutableStateFlow<VersionCheckResultViewEntry?>(null)
    val updateDiagInfo = _updateDiagInfo.asStateFlow()

    sealed class SplashUiState {
        object Idle : SplashUiState()

        object Loading : SplashUiState()

        data class Navigate(
            val action: InitialNavigationAction,
        ) : SplashUiState()
    }

    val uiState: StateFlow<SplashUiState>
    field = MutableStateFlow<SplashUiState>(SplashUiState.Idle)

    private var pendingNavigationState: SplashUiState.Navigate? = null

    init {
        viewModelScope.launch {
            uiState.value = SplashUiState.Loading

            /**
             * El paso 1 y 2 se deben comentar cuando el backend esté caido para evitar problemas
             */

            // 1) Chequeo de versión
            val versionResult =
                withContext(ioDispatcher) {
                    checkAppVersionUseCase(BuildConfig.VERSION_CODE)
                }

            var hasOptionalUpdateGate = false
            when (versionResult) {
                is MiraiLinkResult.Success -> {
                    val info = versionResult.data
                    if (info.mustUpdate) {
                        val viewEntry = info.toVersionCheckResultViewEntry()
                        miraiLinkSession.setForcedUpdate(viewEntry)
                        _updateDiagInfo.value = viewEntry
                        uiState.value = SplashUiState.Idle
                        return@launch
                    } else if (info.shouldUpdate) {
                        _updateDiagInfo.value = info.toVersionCheckResultViewEntry()
                        hasOptionalUpdateGate = true
                    } else {
                        miraiLinkSession.clearForcedUpdate()
                    }
                }

                is MiraiLinkResult.Error -> {
                    // En error de red/config: NO bloquear, continúa normal
                    miraiLinkSession.clearForcedUpdate()
                }
            }

            // Enable Christmas
            store.setChristmasEnabled(isInChristmasMode)

            // 2) Comprobación de Onboarding y Autologin optimizada
            withContext(ioDispatcher) {
                val onboardingResult = checkOnboardingIsCompletedUseCase()

                val nextNavigation =
                    when {
                        // Si el onboarding NO está completado, navegamos de inmediato sin esperar a la red
                        onboardingResult is MiraiLinkResult.Success && !onboardingResult.data -> {
                            SplashUiState.Navigate(
                                InitialNavigationAction.GoToOnboarding,
                            )
                        }

                        // Si el onboarding ya está completado (o falló su lectura local), evaluamos autologin
                        else -> {
                            val autologinResult = autologinUseCase()
                            if (autologinResult is MiraiLinkResult.Success) {
                                SplashUiState.Navigate(
                                    InitialNavigationAction.GoToHome,
                                )
                            } else {
                                SplashUiState.Navigate(
                                    InitialNavigationAction.GoToAuth,
                                )
                            }
                        }
                    }

                withContext(mainDispatcher) {
                    if (hasOptionalUpdateGate && _updateDiagInfo.value?.shouldUpdate == true) {
                        pendingNavigationState = nextNavigation
                        uiState.value = SplashUiState.Idle
                    } else {
                        uiState.value = nextNavigation
                    }
                }
            }
        }
    }

    fun onDismissUpdateGate() {
        _updateDiagInfo.update { it?.copy(mustUpdate = false, shouldUpdate = false) }
        pendingNavigationState?.let { next ->
            uiState.value = next
            pendingNavigationState = null
        }
    }
}
