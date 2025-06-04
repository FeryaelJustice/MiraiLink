package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val autologinUseCase: AutologinUseCase,
) : ViewModel() {

    sealed class SplashUiState {
        object Idle : SplashUiState()
        object Loading : SplashUiState()
        object NavigateToAuth : SplashUiState()
        object NavigateToHome : SplashUiState()
    }

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading
            when (autologinUseCase()) {
                is MiraiLinkResult.Success -> {
                    _uiState.value = SplashUiState.NavigateToHome
                }

                is MiraiLinkResult.Error -> {
                    _uiState.value = SplashUiState.NavigateToAuth
                }
            }
        }
    }
}