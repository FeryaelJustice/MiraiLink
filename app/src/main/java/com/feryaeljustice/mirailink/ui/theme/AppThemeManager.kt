package com.feryaeljustice.mirailink.ui.theme

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.core.featureflags.FLAG_ENABLE_CHRISTMAS_THEME
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlag
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import kotlinx.coroutines.flow.first

class AppThemeManager(
    private val featureFlagStore: FeatureFlagStore,
    private val context: Context,
) {
    enum class ThemeMode {
        DEFAULT,
        CHRISTMAS,
        // en el futuro: HALLOWEEN, ANIME_EVENT, etc
    }

    fun getThemeMode(featureFlags: Map<String, FeatureFlag>): ThemeMode =
        if (featureFlags[FLAG_ENABLE_CHRISTMAS_THEME]?.enabled == true) {
            ThemeMode.CHRISTMAS
        } else {
            ThemeMode.DEFAULT
        }

    suspend fun shouldAppAliasBeUpdatedForThemeMode(): Boolean {
        val currentThemeMode = getThemeMode(featureFlagStore.featureFlagsFlow.first())
        val state =
            context.packageManager.getComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "${BuildConfig.APPLICATION_ID}.${currentThemeMode.name}",
                ),
            )
        return state != PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    suspend fun updateAppAliasForThemeMode() {
        val currentThemeMode = getThemeMode(featureFlagStore.featureFlagsFlow.first())
        for (theme in ThemeMode.entries) {
            val action =
                if (currentThemeMode == theme) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                }

            context.packageManager.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "${BuildConfig.APPLICATION_ID}.${theme.name}",
                ),
                action,
                PackageManager.DONT_KILL_APP,
            )
        }
    }
}
