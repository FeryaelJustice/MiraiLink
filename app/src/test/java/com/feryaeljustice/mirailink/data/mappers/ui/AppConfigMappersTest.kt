/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.VersionCheckResult
import org.junit.Assert.assertEquals
import org.junit.Test

class AppConfigMappersTest {

    @Test
    fun `VersionCheckResult maps to VersionCheckResultViewEntry correctly`() {
        // Given
        val versionCheckResult = VersionCheckResult(
            mustUpdate = true,
            shouldUpdate = false,
            message = "Update available",
            playStoreUrl = "http://play.store.url"
        )

        // When
        val viewEntry = versionCheckResult.toVersionCheckResultViewEntry()

        // Then
        assertEquals(versionCheckResult.mustUpdate, viewEntry.mustUpdate)
        assertEquals(versionCheckResult.shouldUpdate, viewEntry.shouldUpdate)
        assertEquals(versionCheckResult.message, viewEntry.message)
        assertEquals(versionCheckResult.playStoreUrl, viewEntry.playStoreUrl)
    }
}
