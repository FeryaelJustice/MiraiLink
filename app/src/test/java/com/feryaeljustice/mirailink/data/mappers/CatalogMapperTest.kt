package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.KoinTest

class CatalogMapperTest : KoinTest {
    @Test
    fun `AnimeDto maps to Anime domain model correctly`() {
        // Given
        val animeDto =
            AnimeDto(
                id = "anime1",
                name = "Test Anime",
                imageUrl = "http://example.com/anime.jpg",
                catalogKey = "test-anime",
                biography = "Localized biography",
            )

        // When
        val anime = animeDto.toDomain()

        // Then
        assertEquals(animeDto.id, anime.id)
        assertEquals(animeDto.name, anime.name)
        assertEquals(animeDto.imageUrl, anime.imageUrl)
        assertEquals(animeDto.catalogKey, anime.catalogKey)
        assertEquals(animeDto.biography, anime.biography)
    }

    @Test
    fun `GameDto maps to Game domain model correctly`() {
        // Given
        val gameDto =
            GameDto(
                id = "game1",
                name = "Test Game",
                imageUrl = "http://example.com/game.jpg",
                catalogKey = "test-game",
                biography = "Localized biography",
            )

        // When
        val game = gameDto.toDomain()

        // Then
        assertEquals(gameDto.id, game.id)
        assertEquals(gameDto.name, game.name)
        assertEquals(gameDto.imageUrl, game.imageUrl)
        assertEquals(gameDto.catalogKey, game.catalogKey)
        assertEquals(gameDto.biography, game.biography)
    }
}
