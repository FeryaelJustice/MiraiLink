package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.koin.test.KoinTest

class UserMapperTest : KoinTest {
    @Test
    fun `UserDto maps to User domain model correctly`() {
        // Given
        val userDto =
            UserDto(
                id = "user1",
                username = "testuser",
                nickname = "Test",
                email = "test@test.com",
                phoneNumber = "123456789",
                bio = "Bio here",
                gender = "male",
                birthdate = "2000-01-01",
                photos = listOf(UserPhotoDto("photo1", "user1", "url", 1)),
                games = listOf(GameDto("game1", "Game 1", "url")),
                animes = listOf(AnimeDto("anime1", "Anime 1", "url")),
            )

        // When
        val user = userDto.toDomain()

        // Then
        assertEquals("user1", user.id)
        assertEquals("testuser", user.username)
        assertEquals("Test", user.nickname)
        assertEquals("test@test.com", user.email)
        assertEquals(1, user.photos.size)
        assertEquals(1, user.games.size)
        assertEquals(1, user.animes.size)
    }

    @Test
    fun `MinimalUserInfoResponse maps to MinimalUserInfo correctly`() {
        // Given
        val response =
            MinimalUserInfoResponse(
                id = "user1",
                username = "testuser",
                nickname = "Test",
                avatarUrl = "avatar_url",
            )

        // When
        val minimalUser = response.toMinimalUserInfo()

        // Then
        assertEquals("user1", minimalUser.id)
        assertEquals("testuser", minimalUser.username)
        assertEquals("Test", minimalUser.nickname)
        assertEquals("avatar_url", minimalUser.profilePhoto?.url)
    }

    @Test
    fun `UserDto maps to MinimalUserInfo correctly`() {
        // Given
        val userDto =
            UserDto(
                id = "user1",
                username = "testuser",
                nickname = "Test",
                email = "test@test.com",
                gender = "female",
                birthdate = "1995-05-05",
                photos = listOf(UserPhotoDto("photo1", "user1", "url", 1)),
            )

        // When
        val minimalUser = userDto.toMinimalUserInfo()

        // Then
        assertEquals("user1", minimalUser.id)
        assertEquals("testuser", minimalUser.username)
        assertEquals("Test", minimalUser.nickname)
        assertEquals("test@test.com", minimalUser.email)
        assertNotNull(minimalUser.profilePhoto)
    }

    @Test
    fun `User domain model maps to MinimalUserInfo correctly`() {
        // Given
        val user =
            User(
                id = "user1",
                username = "testuser",
                nickname = "Test",
                email = "test@test.com",
                phoneNumber = null,
                bio = null,
                gender = null,
                birthdate = null,
                photos = listOf(UserPhoto("user1", "url", 1)),
                games = emptyList(),
                animes = emptyList(),
            )

        // When
        val minimalUser = user.toMinimalUserInfo()

        // Then
        assertEquals("user1", minimalUser.id)
        assertEquals("testuser", minimalUser.username)
        assertEquals("Test", minimalUser.nickname)
        assertEquals("test@test.com", minimalUser.email)
        assertNotNull(minimalUser.profilePhoto)
    }

    @Test
    fun `UserDto with null profile photo maps to MinimalUserInfo correctly`() {
        // Given
        val userDto =
            UserDto(
                id = "user1",
                username = "testuser",
                nickname = "Test",
                email = "test@test.com",
                photos = emptyList(),
            )

        // When
        val minimalUser = userDto.toMinimalUserInfo()

        // Then
        assertEquals("user1", minimalUser.id)
        assertNull(minimalUser.profilePhoto)
    }
}
