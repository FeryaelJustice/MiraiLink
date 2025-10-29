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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTwoFactorStatusUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: TwoFactorRepository

    private lateinit var getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase

    @Before
    fun onBefore() {
        getTwoFactorStatusUseCase = GetTwoFactorStatusUseCase(repository)
    }

    @Test
    fun `when repository returns true, return success with true`() = runTest {
        // Given
        val userId = "userId"
        coEvery { repository.get2FAStatus(userId) } returns MiraiLinkResult.Success(true)

        // When
        val result = getTwoFactorStatusUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns false, return success with false`() = runTest {
        // Given
        val userId = "userId"
        coEvery { repository.get2FAStatus(userId) } returns MiraiLinkResult.Success(false)

        // When
        val result = getTwoFactorStatusUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertFalse((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to get status, return error`() = runTest {
        // Given
        val userId = "userId"
        val errorResult = MiraiLinkResult.Error("Error getting 2FA status")
        coEvery { repository.get2FAStatus(userId) } returns errorResult

        // When
        val result = getTwoFactorStatusUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val userId = "userId"
        val exception = RuntimeException("Network error")
        coEvery { repository.get2FAStatus(userId) } throws exception

        // When
        val result = getTwoFactorStatusUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while getting 2FA status",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}