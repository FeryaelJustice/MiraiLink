package com.feryaeljustice.mirailink.domain.usecase.photos

import com.feryaeljustice.mirailink.domain.repository.UserRepository
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

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
@ExperimentalCoroutinesApi
class CheckProfilePictureUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var checkProfilePictureUseCase: CheckProfilePictureUseCase

    @Before
    fun onBefore() {
        checkProfilePictureUseCase = CheckProfilePictureUseCase(repo)
    }

    @Test
    fun `when repository returns true, return success with true`() = runTest {
        // Given
        val userId = "userId"
        coEvery { repo.hasProfilePicture(userId) } returns MiraiLinkResult.Success(true)

        // When
        val result = checkProfilePictureUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns false, return success with false`() = runTest {
        // Given
        val userId = "userId"
        coEvery { repo.hasProfilePicture(userId) } returns MiraiLinkResult.Success(false)

        // When
        val result = checkProfilePictureUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertFalse((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val userId = "userId"
        val errorResult = MiraiLinkResult.Error("Error checking profile picture")
        coEvery { repo.hasProfilePicture(userId) } returns errorResult

        // When
        val result = checkProfilePictureUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws exception, return error`() = runTest {
        // Given
        val userId = "userId"
        val exception = RuntimeException("Network error")
        coEvery { repo.hasProfilePicture(userId) } throws exception

        // When
        val result = checkProfilePictureUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("Error checking profile picture", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}