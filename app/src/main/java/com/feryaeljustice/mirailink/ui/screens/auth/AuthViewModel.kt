package com.feryaeljustice.mirailink.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.core.JwtUtils.extractUserId
import com.feryaeljustice.mirailink.data.local.SessionManager
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
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
    private val autologinUseCase: AutologinUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    sealed class AuthUiState {
        object Idle : AuthUiState()
        object Loading : AuthUiState()
        data class Success(val userId: String?) : AuthUiState()
        data class IsAuthenticated(val userId: String?) : AuthUiState()
        data class Error(val message: String, val exception: Throwable? = null) : AuthUiState()
    }

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val res = autologinUseCase()) {
                is MiraiLinkResult.Success -> {
                    _state.value = AuthUiState.IsAuthenticated(res.data)
                }

                is MiraiLinkResult.Error -> {
                    sessionManager.clearSession()
                }
            }
        }
    }

    fun login(email: String, username: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = loginUseCase(email, username, password)) {
                is MiraiLinkResult.Success -> {
                    val userId = extractUserId(result.data)
                    userId?.let {
                        sessionManager.saveSession(result.data, it)
                        withContext(Dispatchers.Main) {
                            _state.value = AuthUiState.Success(it)
                        }
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
                    val userId = extractUserId(result.data)
                    userId?.let {
                        sessionManager.saveSession(result.data, it)
                        withContext(Dispatchers.Main) {
                            _state.value = AuthUiState.Success(it)
                        }
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