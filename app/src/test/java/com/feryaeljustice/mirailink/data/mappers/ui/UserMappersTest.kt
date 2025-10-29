/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMappersTest {

    @Test
    fun `User to UserViewEntry mapping is correct`() {
        // Given
        val user = User(
            id = "user1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            phoneNumber = "123456789",
            bio = "A bio",
            gender = "male",
            birthdate = "2000-01-01",
            photos = listOf(UserPhoto("user1", "photo_url", 1)),
            games = listOf(Game("game1", "Game 1", "game_url")),
            animes = listOf(Anime("anime1", "Anime 1", "anime_url"))
        )

        // When
        val viewEntry = user.toUserViewEntry()

        // Then
        assertEquals("user1", viewEntry.id)
        assertEquals("testuser", viewEntry.username)
        assertEquals("Test User", viewEntry.nickname)
        assertEquals(1, viewEntry.photos.size)
        assertEquals(1, viewEntry.games.size)
        assertEquals(1, viewEntry.animes.size)
    }

    @Test
    fun `User to MatchUserViewEntry mapping is correct`() {
        // Given
        val user = User(
            id = "user1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            phoneNumber = null,
            bio = null,
            gender = null,
            birthdate = null,
            photos = listOf(UserPhoto("user1", "avatar_url", 1)),
            games = emptyList(),
            animes = emptyList()
        )

        // When
        val viewEntry = user.toMatchUserViewEntry()

        // Then
        assertEquals("user1", viewEntry.id)
        assertEquals("testuser", viewEntry.username)
        assertEquals("Test User", viewEntry.nickname)
        assertEquals("avatar_url", viewEntry.avatarUrl)
    }

    @Test
    fun `MinimalUserInfo to MinimalUserInfoViewEntry mapping is correct`() {
        // Given
        val minimalUserInfo = MinimalUserInfo(
            id = "user1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            gender = "female",
            birthdate = "1999-12-12",
            profilePhoto = UserPhoto("user1", "photo_url", 1)
        )

        // When
        val viewEntry = minimalUserInfo.toMinimalUserInfoViewEntry()

        // Then
        assertEquals("user1", viewEntry.id)
        assertEquals("testuser", viewEntry.username)
        assertEquals("Test User", viewEntry.nickname)
        assertEquals("test@example.com", viewEntry.email)
        assertEquals("photo_url", viewEntry.profilePhoto?.url)
    }
}
