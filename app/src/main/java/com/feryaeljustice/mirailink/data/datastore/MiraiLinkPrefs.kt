package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MiraiLinkPrefs(
    private val dataStore: DataStore<AppPrefs>,
) {
    suspend fun markOnboardingCompleted() {
        dataStore.updateData {
            it.copy(onboardingCompleted = true)
        }
    }

    suspend fun isOnboardingCompleted(): Boolean = dataStore.data.first().onboardingCompleted

    fun getThemePreference(): Flow<ThemePreference> =
        dataStore.data
            .catch { emit(AppPrefs()) }
            .map { it.themePreference }

    suspend fun setThemePreference(themePreference: ThemePreference): MiraiLinkResult<Unit> {
        return try {
            dataStore.updateData {
                it.copy(themePreference = themePreference)
            }
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error(DataError.Local.UNKNOWN)
        }
    }
}
