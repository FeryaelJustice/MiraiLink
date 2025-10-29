package com.feryaeljustice.mirailink.domain.usecase.photos

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
 * @since 26/10/2024
 */
@ExperimentalCoroutinesApi
class DeleteUserPhotoUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var deleteUserPhotoUseCase: DeleteUserPhotoUseCase

    @Before
    fun onBefore() {
        deleteUserPhotoUseCase = DeleteUserPhotoUseCase(repo)
    }

    @Test
    fun `when repository deletes photo successfully, return success`() = runTest {
        // Given
        coEvery { repo.deleteUserPhoto(any()) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = deleteUserPhotoUseCase(0)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
    }

    @Test
    fun `when repository fails to delete photo, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Could not delete photo")
        coEvery { repo.deleteUserPhoto(any()) } returns errorResult

        // When
        val result = deleteUserPhotoUseCase(0)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, use case should catch it and return error`() =
        runTest {
            // Given
            val exception = RuntimeException("Network error")
            coEvery { repo.deleteUserPhoto(any()) } throws exception

            // When
            val result = deleteUserPhotoUseCase(0)

            // Then
            assertTrue(result is MiraiLinkResult.Error)
            assertEquals("DeleteUserPhotoUseCase error", (result as MiraiLinkResult.Error).message)
            assertEquals(exception, result.exception)
        }
}