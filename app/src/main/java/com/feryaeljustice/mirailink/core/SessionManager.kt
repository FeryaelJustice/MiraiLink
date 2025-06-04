package com.feryaeljustice.mirailink.core

import com.feryaeljustice.mirailink.data.local.TokenManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    suspend fun notifyLogout() {
        _onLogout.emit(Unit)
    }

    private val _needsToBeVerified = Channel<String>(Channel.UNLIMITED)
    val needsToBeVerified = _needsToBeVerified.receiveAsFlow()

    suspend fun notifyNeedsToBeVerified(userId: String) {
        _needsToBeVerified.send(userId)
    }

    private val _needsToCheckProfilePicture = Channel<Unit>(Channel.UNLIMITED)
    val needsToCheckProfilePicture = _needsToCheckProfilePicture.receiveAsFlow()
    suspend fun notifyNeedsToCheckProfilePicture() {
        _needsToCheckProfilePicture.send(Unit)
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