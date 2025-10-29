/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SetupTwoFactorUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: TwoFactorRepository

    private lateinit var setupTwoFactorUseCase: SetupTwoFactorUseCase

    @Before
    fun onBefore() {
        setupTwoFactorUseCase = SetupTwoFactorUseCase(repository)
    }

    @Test
    fun `when repository sets up 2FA successfully, return success with auth info`() = runTest {
        // Given
        val authInfo = mockk<TwoFactorAuthInfo>()
        coEvery { repository.setup2FA() } returns MiraiLinkResult.Success(authInfo)

        // When
        val result = setupTwoFactorUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(authInfo, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to set up 2FA, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error setting up 2FA")
        coEvery { repository.setup2FA() } returns errorResult

        // When
        val result = setupTwoFactorUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.setup2FA() } throws exception

        // When
        val result = setupTwoFactorUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while setting up 2FA",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}