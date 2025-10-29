/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var remoteDataSource: UserRemoteDataSource
    private lateinit var sessionManager: SessionManager
    private lateinit var repository: UserRepositoryImpl

    private val baseUrl = "http://test.com/"

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        sessionManager = mockk(relaxUnitFun = true)
        repository = UserRepositoryImpl(remoteDataSource, sessionManager, baseUrl)
    }

    @Test
    fun `login success returns success result`() = runBlocking {
        // Given
        val email = "test@test.com"
        val username = "test"
        val password = "password"
        val expectedResult = MiraiLinkResult.Success("token")
        coEvery { remoteDataSource.login(email, username, password) } returns expectedResult

        // When
        val result = repository.login(email, username, password)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `logout clears session and returns success`() = runBlocking {
        // Given
        coEvery { remoteDataSource.logout() } returns MiraiLinkResult.Success(true)

        // When
        val result = repository.logout()

        // Then
        coVerify { sessionManager.clearSession() }
        assertEquals(MiraiLinkResult.Success(true), result)
    }

    @Test
    fun `register success returns success result`() = runBlocking {
        // Given
        val username = "test"
        val email = "test@test.com"
        val password = "password"
        val expectedResult = MiraiLinkResult.Success("token")
        coEvery { remoteDataSource.register(username, email, password) } returns expectedResult

        // When
        val result = repository.register(username, email, password)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getCurrentUser success returns mapped user`() = runBlocking {
        // Given
        val userDto = UserDto(
            "1",
            "test",
            "Test User",
            "bio",
            "Male",
            "2000-01-01",
            "",
            "",
            emptyList(),
            emptyList()
        )
        val photosDto = listOf(UserPhotoDto("1", "1", "photo.jpg", 1))
        val remoteResult = MiraiLinkResult.Success(Pair(userDto, photosDto))
        coEvery { remoteDataSource.getCurrentUser() } returns remoteResult

        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val user = (result as MiraiLinkResult.Success<User>).data
        assertEquals("Test User", user.nickname)
        assertEquals(1, user.photos.size)
        assertEquals("http://test.com/photo.jpg", user.photos[0].url)
    }

    @Test
    fun `getCurrentUser error returns error result`() = runBlocking {
        // Given
        val expectedResult = MiraiLinkResult.Error("error")
        coEvery { remoteDataSource.getCurrentUser() } returns expectedResult

        // When
        val result = repository.getCurrentUser()

        // Then
        assertEquals(expectedResult, result)
    }
}