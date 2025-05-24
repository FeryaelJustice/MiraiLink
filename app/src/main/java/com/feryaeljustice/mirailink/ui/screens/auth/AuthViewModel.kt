package com.feryaeljustice.mirailink.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.local.TokenManager
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.usecase.TestAuthUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val testAuthUseCase: TestAuthUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    sealed class AuthUiState {
        object Idle : AuthUiState()
        object Loading : AuthUiState()
        object Success : AuthUiState()
        object IsAuthenticated : AuthUiState()
        data class Error(val message: String, val exception: Throwable? = null) : AuthUiState()
    }

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (testAuthUseCase()) {
                is MiraiLinkResult.Success -> {
                    _state.value = AuthUiState.IsAuthenticated
                }

                is MiraiLinkResult.Error -> {
                    tokenManager.clearToken()
                }
            }
        }
    }

    fun login(username: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = loginUseCase(username, password)) {
                is MiraiLinkResult.Success -> {
                    tokenManager.saveToken(result.data)
                    withContext(Dispatchers.Main) {
                        _state.value = AuthUiState.Success
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(Dispatchers.Main) {
                        _state.value = AuthUiState.Error(result.message, result.exception)
                    }
                }
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = registerUseCase(username, email, password)) {
                is MiraiLinkResult.Success -> {
                    tokenManager.saveToken(result.data)
                    withContext(Dispatchers.Main) {
                        _state.value = AuthUiState.Success
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(Dispatchers.Main) {
                        _state.value = AuthUiState.Error(result.message, result.exception)
                    }
                }
            }
        }
    }
}