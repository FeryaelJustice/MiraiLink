package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val autologinUseCase: AutologinUseCase,
    private val checkOnboardingIsCompletedUseCase: CheckOnboardingIsCompleted,
) : ViewModel() {

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

            val onboardingDeferred = async(Dispatchers.IO) { checkOnboardingIsCompletedUseCase() }
            val autologinDeferred = async(Dispatchers.IO) { autologinUseCase() }

            val onboardingResult = onboardingDeferred.await()
            val autologinResult = autologinDeferred.await()

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