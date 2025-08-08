package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.domain.model.VersionCheckResult
import com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
    private val autologinUseCase: Lazy<AutologinUseCase>,
    private val checkOnboardingIsCompletedUseCase: Lazy<CheckOnboardingIsCompleted>,
) : ViewModel() {

    private val _updateBlocker = MutableStateFlow<VersionCheckResult?>(null)
    val updateBlocker = _updateBlocker.asStateFlow()

    sealed class SplashUiState {
        object Idle : SplashUiState()
        object Loading : SplashUiState()
        data class Navigate(val action: InitialNavigationAction) : SplashUiState()
    }

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading

            // 1) Chequeo de versión
            val versionResult = withContext(Dispatchers.IO) {
                checkAppVersionUseCase(BuildConfig.VERSION_CODE)
            }
            when (versionResult) {
                is MiraiLinkResult.Success -> {
                    val info = versionResult.data
                    if (info.mustUpdate) {
                        // Bloquea navegación y deja que la UI muestre el diálogo forzando update
                        _updateBlocker.value = info
                        _uiState.value = SplashUiState.Idle
                        return@launch
                    }
                    // (opcional) info.shouldUpdate -> mostrar banner no bloqueante más tarde
                }

                is MiraiLinkResult.Error -> {
                    // En error de red/config: NO bloquear, continúa normal
                }
            }

            // 2) Onboarding + autologin en paralelo
            withContext(Dispatchers.IO) {
                val onboardingDeferred =
                    async { checkOnboardingIsCompletedUseCase.get()() }
                val autologinDeferred = async { autologinUseCase.get()() }

                val onboardingResult = onboardingDeferred.await()
                val autologinResult = autologinDeferred.await()

                withContext(Dispatchers.Main) {
                    _uiState.value = when {
                        onboardingResult is MiraiLinkResult.Success && onboardingResult.data -> {
                            if (autologinResult is MiraiLinkResult.Success) SplashUiState.Navigate(
                                InitialNavigationAction.GoToHome
                            ) else SplashUiState.Navigate(
                                InitialNavigationAction.GoToAuth
                            )
                        }

                        onboardingResult is MiraiLinkResult.Success && !onboardingResult.data -> SplashUiState.Navigate(
                            InitialNavigationAction.GoToOnboarding
                        )

                        autologinResult is MiraiLinkResult.Success -> {
                            SplashUiState.Navigate(InitialNavigationAction.GoToHome)
                        }

                        else -> SplashUiState.Navigate(InitialNavigationAction.GoToAuth)
                    }
                }
            }
        }
    }
}