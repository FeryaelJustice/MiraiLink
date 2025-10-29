/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogMapperTest {

    @Test
    fun `AnimeDto maps to Anime domain model correctly`() {
        // Given
        val animeDto = AnimeDto(
            id = "anime1",
            name = "Test Anime",
            imageUrl = "http://example.com/anime.jpg"
        )

        // When
        val anime = animeDto.toDomain()

        // Then
        assertEquals(animeDto.id, anime.id)
        assertEquals(animeDto.name, anime.name)
        assertEquals(animeDto.imageUrl, anime.imageUrl)
    }

    @Test
    fun `GameDto maps to Game domain model correctly`() {
        // Given
        val gameDto = GameDto(
            id = "game1",
            name = "Test Game",
            imageUrl = "http://example.com/game.jpg"
        )

        // When
        val game = gameDto.toDomain()

        // Then
        assertEquals(gameDto.id, game.id)
        assertEquals(gameDto.name, game.name)
        assertEquals(gameDto.imageUrl, game.imageUrl)
    }
}
