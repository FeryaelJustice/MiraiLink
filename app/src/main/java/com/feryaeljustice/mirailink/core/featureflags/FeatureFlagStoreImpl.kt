package com.feryaeljustice.mirailink.core.featureflags

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "feature_flags")

class FeatureFlagStoreImpl(
    private val context: Context,
) : FeatureFlagStore {
    @Suppress("ktlint:standard:property-naming")
    private val CHRISTMAS_KEY = booleanPreferencesKey(FLAG_ENABLE_CHRISTMAS_THEME)

    override val featureFlagsFlow: Flow<Map<String, FeatureFlag>>
        get() =
            context.dataStore.data.map { prefs ->
                mapOf(
                    FLAG_ENABLE_CHRISTMAS_THEME to
                        FeatureFlag(
                            key = FLAG_ENABLE_CHRISTMAS_THEME,
                            enabled =
                                prefs[CHRISTMAS_KEY]
                                    ?: false,
                        ),
                )
            }

    override suspend fun setChristmasEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[CHRISTMAS_KEY] = enabled
        }
    }
}
