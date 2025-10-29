/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogMappersTest {

    @Test
    fun `Anime to AnimeViewEntry mapping is correct`() {
        // Given
        val anime = Anime(
            id = "anime1",
            name = "Test Anime",
            imageUrl = "http://example.com/anime.jpg"
        )

        // When
        val viewEntry = anime.toAnimeViewEntry()

        // Then
        assertEquals(anime.id, viewEntry.id)
        assertEquals(anime.name, viewEntry.name)
        assertEquals(anime.imageUrl, viewEntry.imageUrl)
    }

    @Test
    fun `Game to GameViewEntry mapping is correct`() {
        // Given
        val game = Game(
            id = "game1",
            name = "Test Game",
            imageUrl = "http://example.com/game.jpg"
        )

        // When
        val viewEntry = game.toGameViewEntry()

        // Then
        assertEquals(game.id, viewEntry.id)
        assertEquals(game.name, viewEntry.name)
        assertEquals(game.imageUrl, viewEntry.imageUrl)
    }
}
