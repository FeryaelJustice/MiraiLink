// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.AppVersionInfoDto
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.KoinTest

class AppVersionInfoMapperTest : KoinTest {

    @Test
    fun `AppVersionInfoDto maps to AppVersionInfo domain model correctly`() {
        // Given
        val appVersionInfoDto = AppVersionInfoDto(
            platform = "Android",
            minVersionCode = 100,
            latestVersionCode = 105,
            message = "Please update the app",
            playStoreUrl = "http://play.google.com/store/apps/details?id=com.feryaeljustice.mirailink"
        )

        // When
        val appVersionInfo = appVersionInfoDto.toDomain()

        // Then
        assertEquals(appVersionInfoDto.platform, appVersionInfo.platform)
        assertEquals(appVersionInfoDto.minVersionCode, appVersionInfo.minVersionCode)
        assertEquals(appVersionInfoDto.latestVersionCode, appVersionInfo.latestVersionCode)
        assertEquals(appVersionInfoDto.message, appVersionInfo.message)
        assertEquals(appVersionInfoDto.playStoreUrl, appVersionInfo.playStoreUrl)
    }
}
