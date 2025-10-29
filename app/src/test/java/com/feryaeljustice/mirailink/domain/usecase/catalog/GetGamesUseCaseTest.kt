/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.catalog

import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
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
class GetGamesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: CatalogRepository

    private lateinit var getGamesUseCase: GetGamesUseCase

    @Before
    fun onBefore() {
        getGamesUseCase = GetGamesUseCase(repository)
    }

    @Test
    fun `when repository returns a list of games, return success with the list`() = runTest {
        // Given
        val games = listOf(mockk<Game>(), mockk<Game>())
        coEvery { repository.getGames() } returns MiraiLinkResult.Success(games)

        // When
        val result = getGamesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(games, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns an empty list, return success with an empty list`() = runTest {
        // Given
        val games = emptyList<Game>()
        coEvery { repository.getGames() } returns MiraiLinkResult.Success(games)

        // When
        val result = getGamesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data.isEmpty())
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error getting games")
        coEvery { repository.getGames() } returns errorResult

        // When
        val result = getGamesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getGames() } throws exception

        // When
        val result = getGamesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while getting the games",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}