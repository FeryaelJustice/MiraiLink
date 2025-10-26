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
class DeleteAccountUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var deleteAccountUseCase: DeleteAccountUseCase

    @Before
    fun onBefore() {
        deleteAccountUseCase = DeleteAccountUseCase(repo)
    }

    @Test
    fun `when repository deletes account successfully, return success`() = runTest {
        // Given
        coEvery { repo.deleteAccount() } returns MiraiLinkResult.Success(Unit)

        // When
        val result = deleteAccountUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
    }

    @Test
    fun `when repository fails to delete account, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Could not delete account")
        coEvery { repo.deleteAccount() } returns errorResult

        // When
        val result = deleteAccountUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repo.deleteAccount() } throws exception

        // When
        val result = deleteAccountUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertTrue((result as MiraiLinkResult.Error).message.contains("DeleteAccountUseCase error:"))
        assertEquals(exception, result.exception)
    }
}