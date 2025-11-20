// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.remote.TwoFactorApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject
import retrofit2.Response

@ExperimentalCoroutinesApi
class TwoFactorRemoteDataSourceTest : UnitTest() {

    private val twoFactorApiService: TwoFactorApiService by inject()
    private val twoFactorRemoteDataSource: TwoFactorRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<TwoFactorApiService>() }
                single { TwoFactorRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `get2FAStatus should return status on success`() = runTest {
        // Given
        val userId = "user123"
        val response = TwoFactorStatusResponse(enabled = true)
        coEvery { twoFactorApiService.getTwoFactorStatus(mapOf("userId" to userId)) } returns response

        // When
        val result = twoFactorRemoteDataSource.get2FAStatus(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(true, (result as MiraiLinkResult.Success).data)
        coVerify { twoFactorApiService.getTwoFactorStatus(mapOf("userId" to userId)) }
    }

    @Test
    fun `setup2FA should return setup details on success`() = runTest {
        // Given
        val response = TwoFactorSetupResponse(
            otpAuthUrl = "otpauth://totp/MiraiLink?secret=BASE32SECRET",
            baseCode = "BASE32SECRET",
            recoveryCodes = listOf("recovery1", "recovery2")
        )
        coEvery { twoFactorApiService.setupTwoFactor() } returns response

        // When
        val result = twoFactorRemoteDataSource.setup2FA()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(response, (result as MiraiLinkResult.Success).data)
        coVerify { twoFactorApiService.setupTwoFactor() }
    }

    @Test
    fun `verify2FA should return success`() = runTest {
        // Given
        val code = "123456"
        coEvery { twoFactorApiService.verifyTwoFactor(mapOf("token" to code)) } returns Response.success(Unit)

        // When
        val result = twoFactorRemoteDataSource.verify2FA(code)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { twoFactorApiService.verifyTwoFactor(mapOf("token" to code)) }
    }

    @Test
    fun `disable2FA should return success`() = runTest {
        // Given
        val code = "123456"
        coEvery { twoFactorApiService.disableTwoFactor(mapOf("code" to code)) } returns Response.success(Unit)

        // When
        val result = twoFactorRemoteDataSource.disable2FA(code)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { twoFactorApiService.disableTwoFactor(mapOf("code" to code)) }
    }

    @Test
    fun `loginVerifyTwoFactorLastStep should return success message`() = runTest {
        // Given
        val userId = "user123"
        val code = "123456"
        val response = BasicResponse("Success")
        coEvery {
            twoFactorApiService.loginVerifyTwoFactorLastStep(mapOf("userId" to userId, "code" to code))
        } returns response

        // When
        val result = twoFactorRemoteDataSource.loginVerifyTwoFactorLastStep(userId, code)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals("Success", (result as MiraiLinkResult.Success).data)
        coVerify { twoFactorApiService.loginVerifyTwoFactorLastStep(mapOf("userId" to userId, "code" to code)) }
    }
}
