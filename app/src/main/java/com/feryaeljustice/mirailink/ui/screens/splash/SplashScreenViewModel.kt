package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

            val result = withContext(Dispatchers.IO) {
                autologinUseCase()
            }

            _uiState.value = when (result) {
                is MiraiLinkResult.Success -> SplashUiState.NavigateToHome
                is MiraiLinkResult.Error -> SplashUiState.NavigateToAuth
            }
        }
    }
}