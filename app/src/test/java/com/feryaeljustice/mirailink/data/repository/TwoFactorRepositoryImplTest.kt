// Feryael Justice
// 2024-07-31

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TwoFactorRepositoryImplTest {

    private lateinit var twoFactorRepository: TwoFactorRepositoryImpl
    private val remoteDataSource: TwoFactorRemoteDataSource = mockk()

    @Before
    fun setUp() {
        twoFactorRepository = TwoFactorRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get2FAStatus returns success`() = runTest {
        // Given
        val userId = "user1"
        coEvery { remoteDataSource.get2FAStatus(userId) } returns MiraiLinkResult.Success(true)

        // When
        val result = twoFactorRepository.get2FAStatus(userId)

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        assertThat((result as MiraiLinkResult.Success).data).isTrue()
    }

    @Test
    fun `setup2FA returns success`() = runTest {
        // Given
        val setupResponse = TwoFactorSetupResponse("otpauth_url", "base32_code", listOf("rec1", "rec2"))
        coEvery { remoteDataSource.setup2FA() } returns MiraiLinkResult.Success(setupResponse)

        // When
        val result = twoFactorRepository.setup2FA()

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val data = (result as MiraiLinkResult.Success).data
        assertThat(data.otpAuthUrl).isEqualTo(setupResponse.otpAuthUrl)
        assertThat(data.baseCode).isEqualTo(setupResponse.baseCode)
    }

    @Test
    fun `verify2FA returns success`() = runTest {
        // Given
        val code = "123456"
        coEvery { remoteDataSource.verify2FA(code) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = twoFactorRepository.verify2FA(code)

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
    }

    @Test
    fun `disable2FA returns success`() = runTest {
        // Given
        val code = "123456"
        coEvery { remoteDataSource.disable2FA(code) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = twoFactorRepository.disable2FA(code)

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
    }

    @Test
    fun `loginVerifyTwoFactorLastStep returns success`() = runTest {
        // Given
        val userId = "user1"
        val code = "123456"
        val token = "jwt_token"
        coEvery { remoteDataSource.loginVerifyTwoFactorLastStep(userId, code) } returns MiraiLinkResult.Success(token)

        // When
        val result = twoFactorRepository.loginVerifyTwoFactorLastStep(userId, code)

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        assertThat((result as MiraiLinkResult.Success).data).isEqualTo(token)
    }
}