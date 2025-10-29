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
class AutologinUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: UserRepository

    private lateinit var autologinUseCase: AutologinUseCase

    @Before
    fun onBefore() {
        autologinUseCase = AutologinUseCase(repository)
    }

    @Test
    fun `when repository autologins successfully, return success with token`() = runTest {
        // Given
        val token = "token"
        coEvery { repository.autologin() } returns MiraiLinkResult.Success(token)

        // When
        val result = autologinUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(token, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to autologin, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("No session found")
        coEvery { repository.autologin() } returns errorResult

        // When
        val result = autologinUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.autologin() } throws exception

        // When
        val result = autologinUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("AutologinUseCase error: ", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}