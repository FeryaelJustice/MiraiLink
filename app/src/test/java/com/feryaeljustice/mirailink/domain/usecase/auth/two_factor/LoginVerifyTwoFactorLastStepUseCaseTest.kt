/**
 * @author Feryael Justice
 * @since 31/10/2024
 */

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginVerifyTwoFactorLastStepUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: TwoFactorRepository

    private lateinit var useCase: LoginVerifyTwoFactorLastStepUseCase

    @Before
    fun onBefore() {
        useCase = LoginVerifyTwoFactorLastStepUseCase(repository)
    }

    @Test
    fun `when repository verifies successfully, return success with LoginResponse`() = runTest {
        // Given
        val challengeToken = "challengeToken"
        val code = "123456"
        val response = LoginResponse(
            token = "newAuthToken",
            userId = "userId",
            requires2FA = false,
            isVerified = true,
        )
        coEvery {
            repository.loginVerifyTwoFactorLastStep(
                challengeToken,
                code,
            )
        } returns MiraiLinkResult.Success(response)

        // When
        val result = useCase(challengeToken, code)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(response, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to verify, return error`() = runTest {
        // Given
        val challengeToken = "challengeToken"
        val code = "123456"
        val errorResult = MiraiLinkResult.Error(UnknownError)
        coEvery { repository.loginVerifyTwoFactorLastStep(challengeToken, code) } returns errorResult

        // When
        val result = useCase(challengeToken, code)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.error, (result as MiraiLinkResult.Error).error)
    }

    @Test(expected = RuntimeException::class)
    fun `when repository throws an exception, propagate exception`() = runTest {
        // Given
        val challengeToken = "challengeToken"
        val code = "123456"
        val exception = RuntimeException("Network error")
        coEvery { repository.loginVerifyTwoFactorLastStep(challengeToken, code) } throws exception

        // When
        useCase(challengeToken, code)
    }
}