package com.feryaeljustice.mirailink.ui.navigation

import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

/** Interaction tests for stable navigation analytics contracts. */
class NavAnalyticsViewModelTest {
    private val analytics = mockk<AnalyticsTracker>(relaxed = true)
    private val viewModel = NavAnalyticsViewModel(analytics)

    /** Verifies the stable event name and route parameter used for screen analytics. */
    @Test
    fun `log screen sends screen view event`() {
        // When
        viewModel.logScreen("home")

        // Then
        verify(exactly = 1) {
            analytics.logEvent("screen_view", mapOf("screen_name" to "home"))
        }
    }

    /** Verifies the stable event name and URI parameter used for deep-link analytics. */
    @Test
    fun `log deep link sends opened event`() {
        // When
        viewModel.logDeepLink("mirailink://profile/42")

        // Then
        verify(exactly = 1) {
            analytics.logEvent(
                "deeplink_opened",
                mapOf("uri" to "mirailink://profile/42"),
            )
        }
    }
}
