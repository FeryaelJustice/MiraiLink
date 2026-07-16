package com.feryaeljustice.mirailink.ui

import app.cash.turbine.test
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlag
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Flow forwarding tests for the application-level ViewModel. */
class MainViewModelTest {
    private val flags = MutableStateFlow<Map<String, FeatureFlag>>(emptyMap())
    private val store = mockk<FeatureFlagStore> {
        every { featureFlagsFlow } returns flags
    }

    /** Verifies that MainViewModel exposes every feature flag update from the store. */
    @Test
    fun `feature flag flow mirrors store updates`() = runTest {
        // Given
        val viewModel = MainViewModel(store)
        val enabled = FeatureFlag(key = "new_home", enabled = true)

        viewModel.featureFlagFlow.test {
            assertThat(awaitItem()).isEmpty()

            // When
            flags.value = mapOf(enabled.key to enabled)

            // Then
            assertThat(awaitItem()).containsExactly(enabled.key, enabled)
        }
    }
}
