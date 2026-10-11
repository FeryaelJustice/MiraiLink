package com.feryaeljustice.mirailink.data.telemetry

import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class CompositeAnalyticsTrackerTest {

    @Test
    fun `logEvent delegates to all trackers and isolates exceptions`() {
        val tracker1 = mockk<AnalyticsTracker>(relaxed = true)
        val tracker2 = mockk<AnalyticsTracker> {
            every { logEvent(any<String>(), any()) } throws RuntimeException("Boom")
        }
        val tracker3 = mockk<AnalyticsTracker>(relaxed = true)

        val composite = CompositeAnalyticsTracker(listOf(tracker1, tracker2, tracker3))

        composite.logEvent("test_event", mapOf("k" to "v"))

        verify(exactly = 1) { tracker1.logEvent("test_event", mapOf("k" to "v")) }
        verify(exactly = 1) { tracker2.logEvent("test_event", mapOf("k" to "v")) }
        verify(exactly = 1) { tracker3.logEvent("test_event", mapOf("k" to "v")) }
    }

    @Test
    fun `logEvent with MiraiAnalyticsEvent delegates to all trackers`() {
        val tracker1 = mockk<AnalyticsTracker>(relaxed = true)
        val tracker2 = mockk<AnalyticsTracker>(relaxed = true)

        val composite = CompositeAnalyticsTracker(listOf(tracker1, tracker2))
        val event = MiraiAnalyticsEvent.SubscriptionEvent.PaywallViewed(triggerSource = "test")

        composite.logEvent(event)

        verify(exactly = 1) { tracker1.logEvent(event) }
        verify(exactly = 1) { tracker2.logEvent(event) }
    }

    @Test
    fun `setUserId and setUserProperty delegate to all trackers`() {
        val tracker1 = mockk<AnalyticsTracker>(relaxed = true)
        val tracker2 = mockk<AnalyticsTracker>(relaxed = true)

        val composite = CompositeAnalyticsTracker(listOf(tracker1, tracker2))

        composite.setUserId("user_123")
        composite.setUserProperty("tier", "premium")

        verify(exactly = 1) { tracker1.setUserId("user_123") }
        verify(exactly = 1) { tracker2.setUserId("user_123") }
        verify(exactly = 1) { tracker1.setUserProperty("tier", "premium") }
        verify(exactly = 1) { tracker2.setUserProperty("tier", "premium") }
    }
}
