/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.catalog

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
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
class GetAnimesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: CatalogRepository

    private lateinit var getAnimesUseCase: GetAnimesUseCase

    @Before
    fun onBefore() {
        getAnimesUseCase = GetAnimesUseCase(repository)
    }

    @Test
    fun `when repository returns a list of animes, return success with the list`() = runTest {
        // Given
        val animes = listOf(mockk<Anime>(), mockk<Anime>())
        coEvery { repository.getAnimes() } returns MiraiLinkResult.Success(animes)

        // When
        val result = getAnimesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(animes, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns an empty list, return success with an empty list`() = runTest {
        // Given
        val animes = emptyList<Anime>()
        coEvery { repository.getAnimes() } returns MiraiLinkResult.Success(animes)

        // When
        val result = getAnimesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data.isEmpty())
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error getting animes")
        coEvery { repository.getAnimes() } returns errorResult

        // When
        val result = getAnimesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getAnimes() } throws exception

        // When
        val result = getAnimesUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while getting the animes",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}