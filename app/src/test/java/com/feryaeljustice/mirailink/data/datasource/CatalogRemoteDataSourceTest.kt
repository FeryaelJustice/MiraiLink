// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.remote.CatalogApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class CatalogRemoteDataSourceTest : UnitTest() {

    private val catalogApiService: CatalogApiService by inject()
    private val catalogRemoteDataSource: CatalogRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<CatalogApiService>() }
                single { CatalogRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getAnimes should return list of animes on success`() = runTest {
        // Given
        val animeList = listOf(AnimeDto(id = "1", name = "Naruto", imageUrl = "url"))
        coEvery { catalogApiService.getAllAnimes() } returns animeList

        // When
        val result = catalogRemoteDataSource.getAnimes()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(animeList, (result as MiraiLinkResult.Success).data)
        coVerify { catalogApiService.getAllAnimes() }
    }

    @Test
    fun `getGames should return list of games on success`() = runTest {
        // Given
        val gameList = listOf(GameDto(id = "1", name = "League of Legends", imageUrl = "url"))
        coEvery { catalogApiService.getAllGames() } returns gameList

        // When
        val result = catalogRemoteDataSource.getGames()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(gameList, (result as MiraiLinkResult.Success).data)
        coVerify { catalogApiService.getAllGames() }
    }
}
