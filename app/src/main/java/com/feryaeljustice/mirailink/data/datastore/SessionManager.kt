package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Clase para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
// que la utilizaremos en el nav wrapper y notificaremos desde cualquier punto de la app
class SessionManager(
    private val dataStore: DataStore<Session>,
    private val scope: CoroutineScope,
) {
    // Caches síncronos para interceptores (actualizados automáticamente)
    @Volatile private var cachedToken: String? = null

    @Volatile private var cachedUserId: String? = null

    @Volatile private var cachedIsVerified: Boolean = false

    // Token & userId flows
    val tokenFlow: Flow<String?> = dataStore.data.map { it.token.ifBlank { null } }
    val userIdFlow: Flow<String?> = dataStore.data.map { it.userId.ifBlank { null } }
    val isVerifiedFlow: Flow<Boolean> = dataStore.data.map { it.verified }

    // True si tiene token y userId válidos
    val isAuthenticatedFlow: Flow<Boolean> =
        combine(tokenFlow, userIdFlow) { token, userId ->
            !token.isNullOrBlank() && !userId.isNullOrBlank()
        }

    // Logout notifier
    private val _onLogout = MutableSharedFlow<Unit>(replay = 0)
    val onLogout: SharedFlow<Unit> = _onLogout.asSharedFlow()

    init {
        scope.launch {
            dataStore.data.collect { session ->
                cachedToken = session.token.ifBlank { null }
                cachedUserId = session.userId.ifBlank { null }
                cachedIsVerified = session.verified
            }
        }
    }

    // MÉTODOS SÍNCRONOS PARA INTERCEPTORES
    fun getCurrentTokenSync(): String? = cachedToken

    // fun getCurrentUserIdSync(): String? = cachedUserId

    // fun getIsVerifiedSync(): Boolean = cachedIsVerified

    fun updateVerificationSync(verified: Boolean) {
        scope.launch { saveIsVerified(verified) }
    }

    fun clearSessionSync() {
        scope.launch { clearSession() }
    }

    // MÉTODOS SUSPEND ORIGINALES
    suspend fun saveSession(
        token: String,
        userId: String,
        verified: Boolean = false,
    ) {
        dataStore.updateData { it.copy(token = token, userId = userId, verified = verified) }
    }

    suspend fun saveIsVerified(isVerified: Boolean) {
        dataStore.updateData { it.copy(verified = isVerified) }
    }

    suspend fun clearSession() {
        dataStore.updateData { Session() }
        _onLogout.emit(Unit)
    }

    suspend fun getCurrentToken(): String? =
        dataStore.data
            .first()
            .token
            .ifBlank { null }
}
