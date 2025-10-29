/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsersRepositoryImplTest {

    private lateinit var remoteDataSource: UsersRemoteDataSource
    private lateinit var repository: UsersRepositoryImpl

    private val baseUrl = "http://test.com/"

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = UsersRepositoryImpl(remoteDataSource, baseUrl)
    }

    @Test
    fun `getUsers success returns mapped users`() = runBlocking {
        // Given
        val photosDto = listOf(UserPhotoDto("1", "1", "photo.jpg", 1))
        val userDto = UserDto(
            id = "1",
            username = "test",
            nickname = "Test User",
            bio = "bio",
            gender = "Male",
            birthdate = "2000-01-01",
            animes = emptyList(),
            games = emptyList(),
            photos = photosDto
        )
        val remoteResult = MiraiLinkResult.Success(listOf(userDto))
        coEvery { remoteDataSource.getUsers() } returns remoteResult

        // When
        val result = repository.getUsers()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val user = (result as MiraiLinkResult.Success<List<User>>).data.first()
        assertEquals("Test User", user.nickname)
        assertEquals(1, user.photos.size)
        assertEquals("http://test.com/photo.jpg", user.photos[0].url)
    }
}