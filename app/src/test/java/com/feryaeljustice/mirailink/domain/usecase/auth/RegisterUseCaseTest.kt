/**
 * @author Feryael Justice
 * @since 31/10/2024
 */

package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.error.UnknownError
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
        val gender = "male"
        val birthdate = "2000-01-01"
        val token = "token"
        coEvery { repository.register(username, email, password, gender, birthdate) } returns MiraiLinkResult.Success(
            token
        )

        // When
        val result = registerUseCase(username, email, password, gender, birthdate)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(token, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when birthdate is underage, return underage error without calling repository`() = runTest {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val gender = "male"
        val birthdate = "2020-01-01"

        // When
        val result = registerUseCase(username, email, password, gender, birthdate)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(com.feryaeljustice.mirailink.domain.error.ValidationError.UNDERAGE, (result as MiraiLinkResult.Error).error)
    }

    @Test
    fun `when repository fails to register, return error`() = runTest {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val gender = "male"
        val birthdate = "2000-01-01"
        val errorResult = MiraiLinkResult.Error(UnknownError)
        coEvery { repository.register(username, email, password, gender, birthdate) } returns errorResult

        // When
        val result = registerUseCase(username, email, password, gender, birthdate)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.error, (result as MiraiLinkResult.Error).error)
    }

    @Test(expected = RuntimeException::class)
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val gender = "male"
        val birthdate = "2000-01-01"
        val exception = RuntimeException("Network error")
        coEvery { repository.register(username, email, password, gender, birthdate) } throws exception

        // When
        val result = registerUseCase(username, email, password, gender, birthdate)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("RegisterUseCase error: ", (result as MiraiLinkResult.Error).error)
        assertEquals(exception, result.error)
    }
}