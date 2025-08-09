package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.feryaeljustice.mirailink.di.SessionDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Clase para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
// que la utilizaremos en el nav wrapper y notificaremos desde cualquier punto de la app
class SessionManager @Inject constructor(
    @param:SessionDataStore private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KEY_TOKEN = stringPreferencesKey("jwt_token")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_VERIFIED = booleanPreferencesKey("verified")
    }

    // Token & userId flows
    val tokenFlow: Flow<String?> = dataStore.data.map { it[KEY_TOKEN] }
    val userIdFlow: Flow<String?> = dataStore.data.map { it[KEY_USER_ID] }
    val isVerifiedFlow: Flow<Boolean> = dataStore.data.map { it[KEY_VERIFIED] ?: false }

    // True si tiene token y userId válidos
    val isAuthenticatedFlow: Flow<Boolean> =
        combine(tokenFlow, userIdFlow) { token, userId ->
            !token.isNullOrBlank() && !userId.isNullOrBlank()
        }

    // Logout notifier
    private val _onLogout = MutableSharedFlow<Unit>(replay = 0)
    val onLogout: SharedFlow<Unit> = _onLogout.asSharedFlow()

    suspend fun saveSession(token: String, userId: String) {
        dataStore.edit {
            it[KEY_TOKEN] = token
            it[KEY_USER_ID] = userId
            it[KEY_VERIFIED] = false
        }
    }

    /*   suspend fun saveUserId(userId: String) {
           dataStore.edit {
               it[KEY_USER_ID] = userId
           }
       }*/

    suspend fun saveIsVerified(isVerified: Boolean) {
        dataStore.edit {
            it[KEY_VERIFIED] = isVerified
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
        _onLogout.emit(Unit)
    }

    //    suspend fun getCurrentUserId(): String? = userIdFlow.first()
    suspend fun getCurrentToken(): String? = tokenFlow.first()
//    suspend fun isVerified(): Boolean = isVerifiedFlow.first()
}