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
class VerifyTwoFactorUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: TwoFactorRepository

    private lateinit var verifyTwoFactorUseCase: VerifyTwoFactorUseCase

    @Before
    fun onBefore() {
        verifyTwoFactorUseCase = VerifyTwoFactorUseCase(repository)
    }

    @Test
    fun `when repository verifies 2FA successfully, return success`() = runTest {
        // Given
        val code = "123456"
        coEvery { repository.verify2FA(code) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = verifyTwoFactorUseCase(code)

        // Then
        assertTrue(result is MiraiLinkResult.Success<*>)
    }

    @Test
    fun `when repository fails to verify 2FA, return error`() = runTest {
        // Given
        val code = "123456"
        val errorResult = MiraiLinkResult.Error("Invalid code")
        coEvery { repository.verify2FA(code) } returns errorResult

        // When
        val result = verifyTwoFactorUseCase(code)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val code = "123456"
        val exception = RuntimeException("Network error")
        coEvery { repository.verify2FA(code) } throws exception

        // When
        val result = verifyTwoFactorUseCase(code)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while verifying 2FA",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}