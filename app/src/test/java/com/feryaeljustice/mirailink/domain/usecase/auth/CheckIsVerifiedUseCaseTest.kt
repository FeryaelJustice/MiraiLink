/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth

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

@ExperimentalCoroutinesApi
class CheckIsVerifiedUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: UserRepository

    private lateinit var checkIsVerifiedUseCase: CheckIsVerifiedUseCase

    @Before
    fun onBefore() {
        checkIsVerifiedUseCase = CheckIsVerifiedUseCase(repository)
    }

    @Test
    fun `when repository returns true, return success with true`() = runTest {
        // Given
        coEvery { repository.checkIsVerified() } returns MiraiLinkResult.Success(true)

        // When
        val result = checkIsVerifiedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns false, return success with false`() = runTest {
        // Given
        coEvery { repository.checkIsVerified() } returns MiraiLinkResult.Success(false)

        // When
        val result = checkIsVerifiedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(false, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error checking verification")
        coEvery { repository.checkIsVerified() } returns errorResult

        // When
        val result = checkIsVerifiedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.checkIsVerified() } throws exception

        // When
        val result = checkIsVerifiedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while checking if the user is verified",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}