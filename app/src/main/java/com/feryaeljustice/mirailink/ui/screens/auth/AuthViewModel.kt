package com.feryaeljustice.mirailink.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.local.SessionManager
import com.feryaeljustice.mirailink.domain.core.JwtUtils.extractUserId
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: Lazy<LoginUseCase>,
    private val registerUseCase: Lazy<RegisterUseCase>,
    private val sessionManager: Lazy<SessionManager>,
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

    fun login(email: String, username: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                loginUseCase.get()(email, username, password)
            }

            handleAuthResult(result)
        }
    }

    fun register(username: String, email: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                registerUseCase.get()(username, email, password)
            }

            handleAuthResult(result)
        }
    }

    private suspend fun handleAuthResult(result: MiraiLinkResult<String>) {
        when (result) {
            is MiraiLinkResult.Success -> {
                val userId = extractUserId(result.data)
                userId?.let {
                    sessionManager.get().saveSession(result.data, it)
                    _state.value = AuthUiState.Success(it)
                } ?: run {
                    _state.value = AuthUiState.Error("No se pudo extraer el ID del usuario")
                }
            }

            is MiraiLinkResult.Error -> {
                _state.value = AuthUiState.Error(result.message, result.exception)
            }
        }
    }

    fun resetUiState() {
        _state.value = AuthUiState.Idle
    }
}