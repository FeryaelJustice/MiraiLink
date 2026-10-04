package com.feryaeljustice.mirailink.data.model.local.datastore

import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import kotlinx.serialization.Serializable

@Serializable
data class AppPrefs(
    val onboardingCompleted: Boolean = false,
    val searchRadiusKm: Float = 40f,
    val searchScope: String = "radius",
    val searchTargetCountryId: String? = null,
    val searchMatchLiveLocation: Boolean = false,
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
)
