/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

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
    fun `when repository verifies successfully, return success with token`() = runTest {
        // Given
        val userId = "userId"
        val code = "123456"
        val token = "newAuthToken"
        coEvery {
            repository.loginVerifyTwoFactorLastStep(
                userId,
                code
            )
        } returns MiraiLinkResult.Success(token)

        // When
        val result = useCase(userId, code)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(token, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to verify, return error`() = runTest {
        // Given
        val userId = "userId"
        val code = "123456"
        val errorResult = MiraiLinkResult.Error("Invalid code")
        coEvery { repository.loginVerifyTwoFactorLastStep(userId, code) } returns errorResult

        // When
        val result = useCase(userId, code)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val userId = "userId"
        val code = "123456"
        val exception = RuntimeException("Network error")
        coEvery { repository.loginVerifyTwoFactorLastStep(userId, code) } throws exception

        // When
        val result = useCase(userId, code)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred during 2FA login verification",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}