package com.feryaeljustice.mirailink.data.telemetry

import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class CompositeCrashReporterTest {

    @Test
    fun `recordException delegates to reporters and correlates with AnalyticsTracker`() {
        val reporter1 = mockk<CrashReporter>(relaxed = true)
        val reporter2 = mockk<CrashReporter> {
            every { recordException(any(), any(), any(), any()) } throws RuntimeException("Reporter error")
        }
        val analytics = mockk<AnalyticsTracker>(relaxed = true)

        val composite = CompositeCrashReporter(
            delegates = listOf(reporter1, reporter2),
            analyticsTracker = analytics,
        )

        val exception = IllegalArgumentException("Invalid state")
        composite.recordException(
            throwable = exception,
            reason = "State was invalid",
            screen = "home_screen",
            metadata = mapOf("extra" to "info"),
        )

        verify(exactly = 1) {
            reporter1.recordException(
                throwable = exception,
                reason = "State was invalid",
                screen = "home_screen",
                metadata = mapOf("extra" to "info"),
            )
        }

        val eventSlot = slot<MiraiAnalyticsEvent>()
        verify(exactly = 1) { analytics.logEvent(capture(eventSlot)) }

        val captured = eventSlot.captured as MiraiAnalyticsEvent.SystemEvent.AppException
        assertEquals("IllegalArgumentException", captured.exceptionType)
        assertEquals("State was invalid", captured.reason)
        assertEquals("home_screen", captured.screen)
        assertEquals(false, captured.isFatal)
        assertEquals("info", captured.metadata["extra"])
    }

    @Test
    fun `log breadcrumbs delegates to all reporters`() {
        val reporter1 = mockk<CrashReporter>(relaxed = true)
        val reporter2 = mockk<CrashReporter>(relaxed = true)

        val composite = CompositeCrashReporter(listOf(reporter1, reporter2))
        composite.log("Navigated to: profile")

        verify(exactly = 1) { reporter1.log("Navigated to: profile") }
        verify(exactly = 1) { reporter2.log("Navigated to: profile") }
    }

    @Test
    fun `setUserId and setKey delegate to all reporters`() {
        val reporter1 = mockk<CrashReporter>(relaxed = true)
        val reporter2 = mockk<CrashReporter>(relaxed = true)

        val composite = CompositeCrashReporter(listOf(reporter1, reporter2))
        composite.setUserId("user_456")
        composite.setKey("subscription_tier", "plus")

        verify(exactly = 1) { reporter1.setUserId("user_456") }
        verify(exactly = 1) { reporter2.setUserId("user_456") }
        verify(exactly = 1) { reporter1.setKey("subscription_tier", "plus") }
        verify(exactly = 1) { reporter2.setKey("subscription_tier", "plus") }
    }
}
