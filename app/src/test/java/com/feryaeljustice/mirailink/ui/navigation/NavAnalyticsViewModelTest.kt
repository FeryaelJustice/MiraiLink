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
            analytics.logEvent(
                match<com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent.NavigationEvent.ScreenView> {
                    it.screenName == "home" && it.eventName == "screen_view"
                },
            )
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
                match<com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent.NavigationEvent.DeepLinkOpened> {
                    it.uri == "mirailink://profile/42" && it.eventName == "deeplink_opened"
                },
            )
        }
    }
}
