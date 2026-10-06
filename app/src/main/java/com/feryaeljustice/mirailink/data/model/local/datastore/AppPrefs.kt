package com.feryaeljustice.mirailink.data.model.local.datastore

import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import kotlinx.serialization.Serializable

@Serializable
data class AppPrefs(
    val demoAffinity: com.feryaeljustice.mirailink.domain.model.affinity.AffinityDemoState = com.feryaeljustice.mirailink.domain.model.affinity.AffinityDemoState(),
    val onboardingCompleted: Boolean = false,
    val searchRadiusKm: Float = 40f,
    val searchScope: String = "radius",
    val searchTargetCountryId: String? = null,
    val searchMatchLiveLocation: Boolean = false,
    val searchGender: String? = null,
    val discoveryMode: String = "classic",
    val demoDiscoveryMode: String = "classic",
    val capsuleSnapshots: Map<String, com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule> = emptyMap(),
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val holoProfileEnabled: Boolean = true,
)
