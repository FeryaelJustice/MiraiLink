package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.test.KoinTest

class TwoFactorAuthMapperTest : KoinTest {
    @Test
    fun `TwoFactorSetupResponse maps to TwoFactorAuthInfo correctly`() {
        // Given
        val response =
            TwoFactorSetupResponse(
                otpAuthUrl = "otp_url",
                baseCode = "base_code",
                recoveryCodes = listOf("rec_code1", "rec_code2"),
            )

        // When
        val authInfo = response.toTwoFactorAuthInfo()

        // Then
        assertEquals("otp_url", authInfo.otpAuthUrl)
        assertEquals("base_code", authInfo.baseCode)
        assertEquals(2, authInfo.recoveryCodes?.size)
    }

    @Test
    fun `TwoFactorStatusResponse maps to TwoFactorAuthInfo correctly`() {
        // Given
        val response = TwoFactorStatusResponse(enabled = true)

        // When
        val authInfo = response.toTwoFactorAuthInfo()

        // Then
        assertTrue(authInfo.enabled == true)
    }
}
