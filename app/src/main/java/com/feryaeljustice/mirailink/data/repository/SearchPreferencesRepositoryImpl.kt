package com.feryaeljustice.mirailink.data.repository

import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SearchPreferencesRepositoryImpl(
    private val dataStore: DataStore<AppPrefs>,
    private val userRemoteDataSource: com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource? = null,
    private val demoModeManager: com.feryaeljustice.mirailink.data.demo.DemoModeManager? = null,
) : SearchPreferencesRepository {

    override fun getSearchPreferences(): Flow<SearchPreferences> =
        dataStore.data
            .catch { emit(AppPrefs()) }
            .map { prefs ->
                SearchPreferences(
                    radiusKm = prefs.searchRadiusKm,
                    scope = try {
                        SearchScope.valueOf(prefs.searchScope.uppercase())
                    } catch (_: Exception) {
                        SearchScope.RADIUS
                    },
                    targetCountryCode = prefs.searchTargetCountry,
                    matchByLiveLocation = prefs.searchMatchLiveLocation,
                    isPremiumActive = false,
                )
            }

    override suspend fun saveSearchPreferences(preferences: SearchPreferences): MiraiLinkResult<Unit> {
        return try {
            dataStore.updateData { current ->
                current.copy(
                    searchRadiusKm = preferences.radiusKm,
                    searchScope = preferences.scope.name.lowercase(),
                    searchTargetCountry = preferences.targetCountryCode,
                    searchMatchLiveLocation = preferences.matchByLiveLocation,
                )
            }
            if (userRemoteDataSource != null && demoModeManager?.isDemoMode?.value != true) {
                // Sync with remote backend in non-demo mode
                userRemoteDataSource.updateSearchSettings(
                    radiusKm = preferences.radiusKm.toInt(),
                    scope = preferences.scope.name.lowercase(),
                    targetCountry = preferences.targetCountryCode,
                    matchLiveLocation = preferences.matchByLiveLocation,
                )
            }
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.DataError.Local.UNKNOWN)
        }
    }
}
