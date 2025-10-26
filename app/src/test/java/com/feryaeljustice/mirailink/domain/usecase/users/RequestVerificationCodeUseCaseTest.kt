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
class RequestVerificationCodeUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var requestVerificationCodeUseCase: RequestVerificationCodeUseCase

    @Before
    fun onBefore() {
        requestVerificationCodeUseCase = RequestVerificationCodeUseCase(repo)
    }

    @Test
    fun `when repository requests verification code successfully, return success`() = runTest {
        // Given
        val successResult = MiraiLinkResult.Success("Verification code sent")
        coEvery { repo.requestVerificationCode(any(), any()) } returns successResult

        // When
        val result = requestVerificationCodeUseCase("userId", "type")

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(successResult.data, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to request verification code, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Could not send verification code")
        coEvery { repo.requestVerificationCode(any(), any()) } returns errorResult

        // When
        val result = requestVerificationCodeUseCase("userId", "type")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repo.requestVerificationCode(any(), any()) } throws exception

        // When
        val result = requestVerificationCodeUseCase("userId", "type")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertTrue((result as MiraiLinkResult.Error).message.contains("RequestPasswordResetUseCase error:"))
        assertEquals(exception, result.exception)
    }
}