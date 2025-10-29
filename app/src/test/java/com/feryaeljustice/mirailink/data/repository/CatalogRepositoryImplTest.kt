/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CatalogRepositoryImplTest {

    private lateinit var remoteDataSource: CatalogRemoteDataSource
    private lateinit var repository: CatalogRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = CatalogRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getAnimes success returns mapped animes`() = runBlocking {
        // Given
        val animeDto = AnimeDto("1", "Anime Title", "")
        val remoteResult = MiraiLinkResult.Success(listOf(animeDto))
        coEvery { remoteDataSource.getAnimes() } returns remoteResult

        // When
        val result = repository.getAnimes()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val anime = (result as MiraiLinkResult.Success<List<Anime>>).data.first()
        assertEquals("Anime Title", anime.name)
    }

    @Test
    fun `getGames success returns mapped games`() = runBlocking {
        // Given
        val gameDto = GameDto("1", "Game Title", "")
        val remoteResult = MiraiLinkResult.Success(listOf(gameDto))
        coEvery { remoteDataSource.getGames() } returns remoteResult

        // When
        val result = repository.getGames()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val game = (result as MiraiLinkResult.Success<List<Game>>).data.first()
        assertEquals("Game Title", game.name)
    }
}