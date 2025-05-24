package com.feryaeljustice.mirailink.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

// Clase para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
// que la utilizaremos en el navwrapper y notificaremos desde cualquier punto de la app
class SessionManager @Inject constructor(){
    private val _onLogout = MutableSharedFlow<Unit>(replay = 0)
    val onLogout = _onLogout.asSharedFlow()

    suspend fun notifyLogout() {
        _onLogout.emit(Unit)
    }
}