package com.feryaeljustice.mirailink.data.model.local.datastore

import kotlinx.serialization.Serializable

@Serializable
data class AppPrefs(
    val onboardingCompleted: Boolean = false
)