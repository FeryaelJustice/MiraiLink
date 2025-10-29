/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coJustRun
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
class LogoutUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: UserRepository

    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun onBefore() {
        logoutUseCase = LogoutUseCase(repository)
    }

    @Test
    fun `when repository logs out successfully, return success`() = runTest {
        // Given
        coJustRun { repository.logout() }

        // When
        val result = logoutUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success<*>)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Logout failed")
        coEvery { repository.logout() } throws exception

        // When
        val result = logoutUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("LogoutUseCase error: ", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}