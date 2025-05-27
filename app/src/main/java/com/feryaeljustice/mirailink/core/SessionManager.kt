package com.feryaeljustice.mirailink.core

import com.feryaeljustice.mirailink.data.local.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

// Clase para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
// que la utilizaremos en el navwrapper y notificaremos desde cualquier punto de la app
class SessionManager @Inject constructor(
    private val tokenManager: TokenManager,
//    private val userIdLocalManager: UserIdLocalManager
) {

    val isSessionManagerInitialized = tokenManager.isTokenManagerInitialized
    val isAuthenticated = tokenManager.isAuthenticated

    private val _onLogout = MutableSharedFlow<Unit>(replay = 0)
    val onLogout = _onLogout.asSharedFlow()

    private val _needsToBeVerified = MutableSharedFlow<String>(replay = 0)
    val needsToBeVerified = _needsToBeVerified.asSharedFlow()

    suspend fun notifyLogout() {
        _onLogout.emit(Unit)
    }

    suspend fun notifyNeedsToBeVerified(userId: String) {
        _needsToBeVerified.emit(userId)
    }

//    suspend fun getUserId() = userIdLocalManager.getUserId()
//
//    suspend fun clearUserId() = userIdLocalManager.clearUserId()
//
//    suspend fun saveUserId(userId: String) = userIdLocalManager.saveUserId(userId)
//
//    suspend fun saveToken(token: String) = tokenManager.saveToken(token)
//
//    suspend fun clearToken() = tokenManager.clearToken()

    suspend fun getToken(): String? = tokenManager.getToken()
}