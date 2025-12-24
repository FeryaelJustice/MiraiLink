package com.feryaeljustice.mirailink.core.featureflags

import kotlinx.coroutines.flow.Flow

interface FeatureFlagStore {
    val featureFlagsFlow: Flow<Map<String, FeatureFlag>>

    suspend fun setChristmasEnabled(enabled: Boolean)
}
