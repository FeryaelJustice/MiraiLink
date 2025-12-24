package com.feryaeljustice.mirailink.ui

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore

class MainViewModel(
    private val featureFlagStore: FeatureFlagStore,
) : ViewModel() {
    val featureFlagFlow = featureFlagStore.featureFlagsFlow
}
