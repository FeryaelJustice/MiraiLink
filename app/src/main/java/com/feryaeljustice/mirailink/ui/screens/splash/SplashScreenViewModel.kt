package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.CheckProfilePictureUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val autologinUseCase: AutologinUseCase,
    private val checkProfilePictureUseCase: CheckProfilePictureUseCase
) : ViewModel() {

    sealed class SplashUiState {
        object Idle : SplashUiState()
        object Loading : SplashUiState()
        object NavigateToAuth : SplashUiState()
        object NavigateToHome : SplashUiState()
        object NavigateToProfilePicture : SplashUiState()
    }

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading
            when (val result = autologinUseCase()) {
                is MiraiLinkResult.Success -> {
                    val userId = result.data
                    checkProfilePicture(userId)
                }

                is MiraiLinkResult.Error -> {
                    _uiState.value = SplashUiState.NavigateToAuth
                }
            }
        }
    }

    private suspend fun checkProfilePicture(userId: String?) {
        if (userId == null) {
            _uiState.value = SplashUiState.NavigateToAuth
            return
        }

        when (val res = checkProfilePictureUseCase(userId)) {
            is MiraiLinkResult.Success -> {
                _uiState.value = if (res.data) {
                    SplashUiState.NavigateToHome
                } else {
                    SplashUiState.NavigateToProfilePicture
                }
            }

            is MiraiLinkResult.Error -> {
                _uiState.value = SplashUiState.NavigateToAuth
            }
        }
    }
}