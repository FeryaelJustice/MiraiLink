/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
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

class SwipeRepositoryImplTest {

    private lateinit var remoteDataSource: SwipeRemoteDataSource
    private lateinit var repository: SwipeRepositoryImpl

    private val baseUrl = "http://test.com/"

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = SwipeRepositoryImpl(remoteDataSource, baseUrl)
    }

    @Test
    fun `getFeed success returns mapped users`() = runBlocking {
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
        coEvery { remoteDataSource.getFeed() } returns remoteResult

        // When
        val result = repository.getFeed()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val user = (result as MiraiLinkResult.Success<List<User>>).data.first()
        assertEquals("Test User", user.nickname)
        assertEquals(1, user.photos.size)
        assertEquals("http://test.com/photo.jpg", user.photos[0].url)
    }

    @Test
    fun `likeUser calls remote and returns result`() = runBlocking {
        // Given
        val userId = "1"
        val expectedResult = MiraiLinkResult.Success(true)
        coEvery { remoteDataSource.likeUser(userId) } returns expectedResult

        // When
        val result = repository.likeUser(userId)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `dislikeUser calls remote and returns result`() = runBlocking {
        // Given
        val userId = "1"
        val expectedResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.dislikeUser(userId) } returns expectedResult

        // When
        val result = repository.dislikeUser(userId)

        // Then
        assertEquals(expectedResult, result)
    }
}