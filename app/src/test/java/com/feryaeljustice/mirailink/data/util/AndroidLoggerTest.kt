package com.feryaeljustice.mirailink.data.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

/** Robolectric contract test for the Android logging adapter. */
@RunWith(RobolectricTestRunner::class)
class AndroidLoggerTest {
    /** Verifies debug messages retain their tag, level and content at the platform boundary. */
    @Test
    fun `debug delegates to android log`() {
        // Given
        val logger = AndroidLogger()

        // When
        logger.d(tag = "MiraiLink", message = "Loaded")

        // Then
        val entry = ShadowLog.getLogsForTag("MiraiLink").single()
        assertThat(entry.type).isEqualTo(android.util.Log.DEBUG)
        assertThat(entry.msg).isEqualTo("Loaded")
    }
}
