/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.feed

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
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
class GetFeedUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: SwipeRepository

    private lateinit var getFeedUseCase: GetFeedUseCase

    @Before
    fun onBefore() {
        getFeedUseCase = GetFeedUseCase(repository)
    }

    @Test
    fun `when repository returns a list of users, return success with the list`() = runTest {
        // Given
        val users = listOf(mockk<User>(), mockk<User>())
        coEvery { repository.getFeed() } returns MiraiLinkResult.Success(users)

        // When
        val result = getFeedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(users, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns an empty list, return success with an empty list`() = runTest {
        // Given
        val users = emptyList<User>()
        coEvery { repository.getFeed() } returns MiraiLinkResult.Success(users)

        // When
        val result = getFeedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data.isEmpty())
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error getting feed")
        coEvery { repository.getFeed() } returns errorResult

        // When
        val result = getFeedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getFeed() } throws exception

        // When
        val result = getFeedUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("GetFeedUseCase error", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}