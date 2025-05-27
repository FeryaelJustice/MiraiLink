package com.feryaeljustice.mirailink.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    private val _isTokenManagerInitialized = MutableStateFlow(false)
    val isTokenManagerInitialized: StateFlow<Boolean> = _isTokenManagerInitialized.asStateFlow()

    val isAuthenticated: Flow<Boolean> = dataStore.data.map {
        !it[JWT_TOKEN_KEY].isNullOrEmpty()
    }.catch { emit(false) }.onEach { _isTokenManagerInitialized.value = true }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[JWT_TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(JWT_TOKEN_KEY) }
    }

    suspend fun getToken(): String? {
        return dataStore.data.map { it[JWT_TOKEN_KEY] }.first()
    }
}
