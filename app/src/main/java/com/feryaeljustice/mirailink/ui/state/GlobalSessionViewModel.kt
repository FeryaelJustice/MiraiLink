package com.feryaeljustice.mirailink.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.core.SessionManager
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.ui.components.TopBarConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
@HiltViewModel
class GlobalSessionViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    val isGlobalSessionInitialized = sessionManager.isSessionManagerInitialized
    val isAuthenticated = sessionManager.isAuthenticated
    val needsToBeVerified = sessionManager.needsToBeVerified

    private val _onLogout = MutableSharedFlow<Unit>(replay = 0)
    val onLogout = _onLogout.asSharedFlow()

    private val _topBarConfig = MutableStateFlow(TopBarConfig())
    val topBarConfig = _topBarConfig.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.onLogout.collect {
                logout()
            }
        }
    }

    suspend fun logout() {
        logoutUseCase() // llama a logout
        _onLogout.emit(Unit) // notifica a NavWrapper
    }

    fun updateTopBar(config: TopBarConfig) {
        _topBarConfig.value = config
    }

//    suspend fun getUserId() = sessionManager.getUserId()
//
//    suspend fun clearUserId() = sessionManager.clearUserId()
//
//    suspend fun saveUserId(userId: String) = sessionManager.saveUserId(userId)
//
//    suspend fun saveToken(token: String) = sessionManager.saveToken(token)
//
//    suspend fun clearToken() = sessionManager.clearToken()
//
//    suspend fun getToken(): String? = sessionManager.getToken()
}