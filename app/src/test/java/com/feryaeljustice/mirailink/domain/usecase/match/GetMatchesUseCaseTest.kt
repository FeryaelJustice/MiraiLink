package com.feryaeljustice.mirailink.domain.usecase.match

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
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

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
@ExperimentalCoroutinesApi
class GetMatchesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: MatchRepository

    private lateinit var getMatchesUseCase: GetMatchesUseCase

    @Before
    fun onBefore() {
        getMatchesUseCase = GetMatchesUseCase(repo)
    }

    @Test
    fun `when repository returns a list of matches, return success with the list`() = runTest {
        // Given
        val matches = listOf(mockk<User>(), mockk<User>())
        coEvery { repo.getMatches() } returns MiraiLinkResult.Success(matches)

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(matches, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns an empty list, return success with an empty list`() = runTest {
        // Given
        val matches = emptyList<User>()
        coEvery { repo.getMatches() } returns MiraiLinkResult.Success(matches)

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data.isEmpty())
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error getting matches")
        coEvery { repo.getMatches() } returns errorResult

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repo.getMatches() } throws exception

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("GetMatchesUseCase error", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}