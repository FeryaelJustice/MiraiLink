package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import com.feryaeljustice.mirailink.ui.viewentries.VersionCheckResultViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashScreenViewModel(
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
    private val autologinUseCase: AutologinUseCase,
    private val checkOnboardingIsCompletedUseCase: CheckOnboardingIsCompleted,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
    private val store: FeatureFlagStore,
    private val isInChristmasMode: Boolean,
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

    init {
        viewModelScope.launch {
            uiState.value = SplashUiState.Loading
            delay(1000)
            withContext(Dispatchers.Main){
                uiState.value = SplashUiState.Navigate(InitialNavigationAction.GoToAuth)
            }

            /**
             * VOLVER A DESCOMENTAR ESTO CUANDO BACKEND REVIVA, bloquea el main thread
             */
            /*
            // 1) Chequeo de versión
            val versionResult =
                withContext(ioDispatcher) {
                    checkAppVersionUseCase(BuildConfig.VERSION_CODE)
                }
            when (versionResult) {
                is MiraiLinkResult.Success -> {
                    val info = versionResult.data
                    if (info.mustUpdate || info.shouldUpdate) {
                        // Deja que la UI muestre el diálogo forzando update
                        _updateDiagInfo.value = info.toVersionCheckResultViewEntry()
                        uiState.value = SplashUiState.Idle
                    }
                }

                is MiraiLinkResult.Error -> {
                    // En error de red/config: NO bloquear, continúa normal
                }
            }

            // Enable Christmas
            Log.d("SplashScreenViewModel", "Christmas mode enabled: $isInChristmasMode")
            store.setChristmasEnabled(isInChristmasMode)

            // 2) Onboarding + autologin en paralelo
            withContext(ioDispatcher) {
                val onboardingDeferred =
                    async { checkOnboardingIsCompletedUseCase() }
                val autologinDeferred = async { autologinUseCase() }

                val onboardingResult = onboardingDeferred.await()
                val autologinResult = autologinDeferred.await()

                withContext(mainDispatcher) {
                    uiState.value =
                        when {
                            onboardingResult is MiraiLinkResult.Success && onboardingResult.data -> {
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

                            onboardingResult is MiraiLinkResult.Success && !onboardingResult.data -> {
                                SplashUiState.Navigate(
                                    InitialNavigationAction.GoToOnboarding,
                                )
                            }

                            autologinResult is MiraiLinkResult.Success -> {
                                SplashUiState.Navigate(InitialNavigationAction.GoToHome)
                            }

                            else -> {
                                SplashUiState.Navigate(InitialNavigationAction.GoToAuth)
                            }
                        }
                }
            }
            */
        }
    }

    fun onDismissUpdateGate() {
        _updateDiagInfo.update { it?.copy(mustUpdate = false, shouldUpdate = false) }
    }
}
