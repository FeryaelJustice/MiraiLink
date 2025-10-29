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
class LoginUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: UserRepository

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun onBefore() {
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `when repository logs in successfully, return success with token`() = runTest {
        // Given
        val email = "test@test.com"
        val username = "test"
        val password = "password"
        val token = "token"
        coEvery { repository.login(email, username, password) } returns MiraiLinkResult.Success(
            token
        )

        // When
        val result = loginUseCase(email, username, password)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(token, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to log in, return error`() = runTest {
        // Given
        val email = "test@test.com"
        val username = "test"
        val password = "password"
        val errorResult = MiraiLinkResult.Error("Invalid credentials")
        coEvery { repository.login(email, username, password) } returns errorResult

        // When
        val result = loginUseCase(email, username, password)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val email = "test@test.com"
        val username = "test"
        val password = "password"
        val exception = RuntimeException("Network error")
        coEvery { repository.login(email, username, password) } throws exception

        // When
        val result = loginUseCase(email, username, password)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("LoginUseCase error: ", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}