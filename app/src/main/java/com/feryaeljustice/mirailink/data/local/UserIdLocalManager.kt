package com.feryaeljustice.mirailink.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/*
class UserIdLocalManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { it[USER_ID_KEY] = userId }
    }

    suspend fun clearUserId() {
        dataStore.edit { it.remove(USER_ID_KEY) }
    }

    suspend fun getUserId(): String? {
        return dataStore.data.map { it[USER_ID_KEY] }.first()
    }
}
*/
