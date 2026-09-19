package com.feryaeljustice.mirailink.data.model.local.datastore

import kotlinx.serialization.Serializable

@Serializable
data class AppPrefs(
    val onboardingCompleted: Boolean = false,
    val searchRadiusKm: Float = 40f,
    val searchScope: String = "radius",
    val searchTargetCountry: String? = null,
    val searchMatchLiveLocation: Boolean = false,
)