/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TwoFactorRepositoryImplTest {

    private lateinit var remoteDataSource: TwoFactorRemoteDataSource
    private lateinit var repository: TwoFactorRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = TwoFactorRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get2FAStatus calls remote and returns result`() = runBlocking {
        // Given
        val userId = "1"
        val expectedResult = MiraiLinkResult.Success(true)
        coEvery { remoteDataSource.get2FAStatus(userId) } returns expectedResult

        // When
        val result = repository.get2FAStatus(userId)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `setup2FA success returns mapped auth info`() = runBlocking {
        // Given
        val setupResponse = TwoFactorSetupResponse("otpauth://url", "base32", listOf("recovery1"))
        val remoteResult = MiraiLinkResult.Success(setupResponse)
        coEvery { remoteDataSource.setup2FA() } returns remoteResult

        // When
        val result = repository.setup2FA()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val authInfo = (result as MiraiLinkResult.Success<TwoFactorAuthInfo>).data
        assertEquals("otpauth://url", authInfo.otpAuthUrl)
        assertEquals("base32", authInfo.baseCode)
        assertEquals(listOf("recovery1"), authInfo.recoveryCodes)
    }

    @Test
    fun `verify2FA calls remote and returns result`() = runBlocking {
        // Given
        val code = "123456"
        val expectedResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.verify2FA(code) } returns expectedResult

        // When
        val result = repository.verify2FA(code)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `disable2FA calls remote and returns result`() = runBlocking {
        // Given
        val code = "123456"
        val expectedResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.disable2FA(code) } returns expectedResult

        // When
        val result = repository.disable2FA(code)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `loginVerifyTwoFactorLastStep calls remote and returns result`() = runBlocking {
        // Given
        val userId = "1"
        val code = "123456"
        val expectedResult = MiraiLinkResult.Success("Success")
        coEvery {
            remoteDataSource.loginVerifyTwoFactorLastStep(
                userId,
                code
            )
        } returns expectedResult

        // When
        val result = repository.loginVerifyTwoFactorLastStep(userId, code)

        // Then
        assertEquals(expectedResult, result)
    }
}