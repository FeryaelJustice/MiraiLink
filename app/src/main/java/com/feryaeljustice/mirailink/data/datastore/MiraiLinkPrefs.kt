package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.feryaeljustice.mirailink.di.PrefsDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class MiraiLinkPrefs @Inject constructor(@param:PrefsDataStore private val dataStore: DataStore<Preferences>) {

    companion object MiraiPrefsKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    suspend fun markOnboardingCompleted() {
        dataStore.edit {
            it[ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun isOnboardingCompleted(): Boolean {
        return dataStore.data.first()[ONBOARDING_COMPLETED] ?: false
    }
}