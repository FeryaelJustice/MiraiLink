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
                    scope = SearchScope.fromWireValue(prefs.searchScope, prefs.searchMatchLiveLocation),
                    targetCountryCode = prefs.searchTargetCountry,
                    isPremiumActive = false,
                )
            }

    override suspend fun saveSearchPreferences(preferences: SearchPreferences): MiraiLinkResult<Unit> {
        return try {
            val normalized = preferences.copy(
                targetCountryCode = preferences.targetCountryCode.takeIf {
                    preferences.scope == SearchScope.SPECIFIC_COUNTRY
                },
            )
            if (userRemoteDataSource != null && demoModeManager?.isDemoMode?.value != true) {
                when (val remoteResult = userRemoteDataSource.updateSearchSettings(
                    radiusKm = normalized.radiusKm.toInt(),
                    scope = normalized.scope.wireValue,
                    targetCountry = normalized.targetCountryCode,
                    matchLiveLocation = false,
                )) {
                    is MiraiLinkResult.Error -> return remoteResult
                    is MiraiLinkResult.Success -> Unit
                }
            }
            dataStore.updateData { current ->
                current.copy(
                    searchRadiusKm = normalized.radiusKm,
                    searchScope = normalized.scope.wireValue,
                    searchTargetCountry = normalized.targetCountryCode,
                    searchMatchLiveLocation = false,
                )
            }
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.DataError.Local.UNKNOWN)
        }
    }
}
