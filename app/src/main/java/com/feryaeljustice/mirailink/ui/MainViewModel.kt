package com.feryaeljustice.mirailink.ui

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.usecase.settings.GetThemePreferenceUseCase
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    private val featureFlagStore: FeatureFlagStore,
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase,
) : ViewModel() {
    val featureFlagFlow = featureFlagStore.featureFlagsFlow
    val themePreferenceFlow: Flow<ThemePreference> = getThemePreferenceUseCase()
}
