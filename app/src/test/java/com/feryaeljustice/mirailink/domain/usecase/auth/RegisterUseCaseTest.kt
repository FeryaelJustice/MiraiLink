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
class RegisterUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: UserRepository

    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun onBefore() {
        registerUseCase = RegisterUseCase(repository)
    }

    @Test
    fun `when repository registers successfully, return success with token`() = runTest {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val token = "token"
        coEvery { repository.register(username, email, password) } returns MiraiLinkResult.Success(
            token
        )

        // When
        val result = registerUseCase(username, email, password)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(token, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to register, return error`() = runTest {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val errorResult = MiraiLinkResult.Error("User already exists")
        coEvery { repository.register(username, email, password) } returns errorResult

        // When
        val result = registerUseCase(username, email, password)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val exception = RuntimeException("Network error")
        coEvery { repository.register(username, email, password) } throws exception

        // When
        val result = registerUseCase(username, email, password)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("RegisterUseCase error: ", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}