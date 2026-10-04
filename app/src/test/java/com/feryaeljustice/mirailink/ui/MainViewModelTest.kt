package com.feryaeljustice.mirailink.ui

import app.cash.turbine.test
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlag
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.usecase.settings.GetThemePreferenceUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Flow forwarding tests for the application-level ViewModel. */
class MainViewModelTest {
    private val flags = MutableStateFlow<Map<String, FeatureFlag>>(emptyMap())
    private val themeFlow = MutableStateFlow(ThemePreference.SYSTEM)
    private val store = mockk<FeatureFlagStore> {
        every { featureFlagsFlow } returns flags
    }
    private val getThemePreferenceUseCase = mockk<GetThemePreferenceUseCase> {
        every { this@mockk.invoke() } returns themeFlow
    }

    /** Verifies that MainViewModel exposes every feature flag update from the store. */
    @Test
    fun `feature flag flow mirrors store updates`() = runTest {
        // Given
        val viewModel = MainViewModel(store, getThemePreferenceUseCase)
        val enabled = FeatureFlag(key = "new_home", enabled = true)

        viewModel.featureFlagFlow.test {
            assertThat(awaitItem()).isEmpty()

            // When
            flags.value = mapOf(enabled.key to enabled)

            // Then
            assertThat(awaitItem()).containsExactly(enabled.key, enabled)
        }
    }

    @Test
    fun `theme preference flow mirrors use case updates`() = runTest {
        // Given
        val viewModel = MainViewModel(store, getThemePreferenceUseCase)

        viewModel.themePreferenceFlow.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.SYSTEM)

            // When
            themeFlow.value = ThemePreference.DARK

            // Then
            assertThat(awaitItem()).isEqualTo(ThemePreference.DARK)
        }
    }
}
