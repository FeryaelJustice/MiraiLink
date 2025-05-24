package com.feryaeljustice.mirailink.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.core.SessionManager
import com.feryaeljustice.mirailink.data.local.TokenManager
import com.feryaeljustice.mirailink.domain.usecase.LogoutUseCase
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
    private val tokenManager: TokenManager,
    private val logoutUseCase: LogoutUseCase,
    val sessionManager: SessionManager
) : ViewModel() {

    val isAuthenticated = tokenManager.isAuthenticated

    val isInitialized = tokenManager.isInitialized

    private val _onLogout = MutableSharedFlow<Unit>()
    val onLogout = _onLogout.asSharedFlow()

    private val _topBarConfig = MutableStateFlow(TopBarConfig())
    val topBarConfig = _topBarConfig.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.onLogout.collect {
                logoutUseCase() // llama a logout
                _onLogout.emit(Unit) // notifica a NavWrapper
            }
        }
    }

    fun updateTopBar(config: TopBarConfig) {
        _topBarConfig.value = config
    }
}