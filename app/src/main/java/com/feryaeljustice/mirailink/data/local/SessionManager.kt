package com.feryaeljustice.mirailink.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "session_prefs")

// Clase para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
// que la utilizaremos en el nav wrapper y notificaremos desde cualquier punto de la app
class SessionManager @Inject constructor(
    private val context: Context
) {
    companion object {
        val KEY_TOKEN = stringPreferencesKey("jwt_token")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_VERIFIED = booleanPreferencesKey("verified")
    }

    // Token & userId flows
    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }
    val userIdFlow: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val isVerifiedFlow: Flow<Boolean> = context.dataStore.data.map { it[KEY_VERIFIED] ?: false }

    // True si tiene token y userId válidos
    val isAuthenticatedFlow: Flow<Boolean> =
        combine(tokenFlow, userIdFlow) { token, userId ->
            !token.isNullOrBlank() && !userId.isNullOrBlank()
        }

    // Logout notifier
    private val _onLogout = MutableSharedFlow<Unit>(replay = 0)
    val onLogout: SharedFlow<Unit> = _onLogout.asSharedFlow()

    suspend fun saveSession(token: String, userId: String) {
        context.dataStore.edit {
            it[KEY_TOKEN] = token
            it[KEY_USER_ID] = userId
            it[KEY_VERIFIED] = false
        }
    }

    /*   suspend fun saveUserId(userId: String) {
           context.dataStore.edit {
               it[KEY_USER_ID] = userId
           }
       }*/

    suspend fun saveIsVerified(isVerified: Boolean) {
        context.dataStore.edit {
            it[KEY_VERIFIED] = isVerified
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
        _onLogout.emit(Unit)
    }

    //    suspend fun getCurrentUserId(): String? = userIdFlow.first()
    suspend fun getCurrentToken(): String? = tokenFlow.first()
//    suspend fun isVerified(): Boolean = isVerifiedFlow.first()
}