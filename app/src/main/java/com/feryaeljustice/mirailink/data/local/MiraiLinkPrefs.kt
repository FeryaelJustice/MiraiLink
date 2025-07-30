package com.feryaeljustice.mirailink.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.prefsDataStore by preferencesDataStore(name = "mirailink_prefs")

class MiraiLinkPrefs @Inject constructor(private val context: Context) {

    companion object MiraiPrefsKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    suspend fun markOnboardingCompleted() {
        context.prefsDataStore.edit {
            it[ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun isOnboardingCompleted(): Boolean {
        return context.prefsDataStore.data.first()[ONBOARDING_COMPLETED] ?: false
    }
}