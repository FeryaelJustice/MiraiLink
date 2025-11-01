// Feryael Justice
// 2024-07-31

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CatalogRepositoryImplTest {

    private lateinit var catalogRepository: CatalogRepositoryImpl
    private val remoteDataSource: CatalogRemoteDataSource = mockk()

    private val animeDto = AnimeDto(id = "1", name = "Anime Test", imageUrl = null)
    private val gameDto = GameDto(id = "1", name = "Game Test", imageUrl = null)

    @Before
    fun setUp() {
        catalogRepository = CatalogRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getAnimes returns success when remote data source is successful`() = runTest {
        // Given
        val animeDtoList = listOf(animeDto)
        val successResult = MiraiLinkResult.Success(animeDtoList)
        coEvery { remoteDataSource.getAnimes() } returns successResult

        // When
        val result = catalogRepository.getAnimes()

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val animes = (result as MiraiLinkResult.Success).data
        assertThat(animes).hasSize(1)
        assertThat(animes.first().id).isEqualTo(animeDto.id)
    }

    @Test
    fun `getAnimes returns error when remote data source fails`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("An error occurred")
        coEvery { remoteDataSource.getAnimes() } returns errorResult

        // When
        val result = catalogRepository.getAnimes()

        // Then
        assertThat(result).isEqualTo(errorResult)
    }

    @Test
    fun `getGames returns success when remote data source is successful`() = runTest {
        // Given
        val gameDtoList = listOf(gameDto)
        val successResult = MiraiLinkResult.Success(gameDtoList)
        coEvery { remoteDataSource.getGames() } returns successResult

        // When
        val result = catalogRepository.getGames()

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val games = (result as MiraiLinkResult.Success).data
        assertThat(games).hasSize(1)
        assertThat(games.first().id).isEqualTo(gameDto.id)
    }

    @Test
    fun `getGames returns error when remote data source fails`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("An error occurred")
        coEvery { remoteDataSource.getGames() } returns errorResult

        // When
        val result = catalogRepository.getGames()

        // Then
        assertThat(result).isEqualTo(errorResult)
    }
}