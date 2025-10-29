package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
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

/**
 * @author Feryael Justice
 * @since 18/10/2024
 */
@ExperimentalCoroutinesApi
class RequestPasswordResetUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var requestPasswordResetUseCase: RequestPasswordResetUseCase

    @Before
    fun onBefore() {
        requestPasswordResetUseCase = RequestPasswordResetUseCase(repo)
    }

    @Test
    fun `when repository requests password reset successfully, return success`() = runTest {
        // Given
        val successResult = MiraiLinkResult.Success("Password reset email sent")
        coEvery { repo.requestPasswordReset(any()) } returns successResult

        // When
        val result = requestPasswordResetUseCase("test@example.com")

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(successResult.data, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to request password reset, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("User not found")
        coEvery { repo.requestPasswordReset(any()) } returns errorResult

        // When
        val result = requestPasswordResetUseCase("test@example.com")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repo.requestPasswordReset(any()) } throws exception

        // When
        val result = requestPasswordResetUseCase("test@example.com")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("RequestPasswordResetUseCase error", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}