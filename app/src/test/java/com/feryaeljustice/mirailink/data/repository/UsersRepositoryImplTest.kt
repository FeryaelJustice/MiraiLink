// Feryael Justice
// 2024-07-31

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UsersRepositoryImplTest {

    private lateinit var usersRepository: UsersRepositoryImpl
    private val usersRemoteDataSource: UsersRemoteDataSource = mockk()

    private val userDto = UserDto(
        id = "1",
        username = "testuser",
        nickname = "Test User",
        email = "test@example.com",
        photos = listOf(
            UserPhotoDto(
                id = "photo1",
                userId = "1",
                url = "/path/to/photo.jpg",
                position = 1
            )
        ),
        animes = emptyList(),
        games = emptyList()
    )

    @Before
    fun setUp() {
        usersRepository = UsersRepositoryImpl(usersRemoteDataSource, "http://localhost:8080")
    }

    @Test
    fun `getUsers returns success when remote data source is successful`() = runTest {
        // Given
        val userDtoList = listOf(userDto)
        val successResult = MiraiLinkResult.Success(userDtoList)
        coEvery { usersRemoteDataSource.getUsers() } returns successResult

        // When
        val result = usersRepository.getUsers()

        // Then
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val users = (result as MiraiLinkResult.Success).data
        assertThat(users).hasSize(1)
        assertThat(users.first().id).isEqualTo(userDto.id)
        assertThat(users.first().username).isEqualTo(userDto.username)
        assertThat(users.first().photos.first().url).endsWith(userDto.photos.first().url)
    }

    @Test
    fun `getUsers returns error when remote data source fails`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("An error occurred")
        coEvery { usersRemoteDataSource.getUsers() } returns errorResult

        // When
        val result = usersRepository.getUsers()

        // Then
        assertThat(result).isEqualTo(errorResult)
    }
}