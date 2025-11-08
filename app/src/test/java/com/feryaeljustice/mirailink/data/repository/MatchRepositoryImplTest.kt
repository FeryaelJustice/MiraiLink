// Feryael Justice
// 2025-11-08

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource
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
class MatchRepositoryImplTest {
    private lateinit var matchRepository: MatchRepositoryImpl
    private val matchRemoteDataSource: MatchRemoteDataSource = mockk()

    private val userDto =
        UserDto(
            id = "1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            photos =
                listOf(
                    UserPhotoDto(
                        id = "photo1",
                        userId = "1",
                        url = "/path/to/photo.jpg",
                        position = 1,
                    ),
                ),
            animes = emptyList(),
            games = emptyList(),
        )

    @Before
    fun setUp() {
        matchRepository = MatchRepositoryImpl(matchRemoteDataSource, "http://localhost:8080")
    }

    @Test
    fun `getMatches returns success when remote data source is successful`() =
        runTest {
            // Given
            val userDtoList = listOf(userDto)
            val successResult = MiraiLinkResult.Success(userDtoList)
            coEvery { matchRemoteDataSource.getMatches() } returns successResult

            // When
            val result = matchRepository.getMatches()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val users = (result as MiraiLinkResult.Success).data
            assertThat(users).hasSize(1)
            assertThat(users.first().id).isEqualTo(userDto.id)
            assertThat(
                users
                    .first()
                    .photos
                    .first()
                    .url,
            ).endsWith(userDto.photos.first().url)
        }

    @Test
    fun `getMatches returns error when remote data source fails`() =
        runTest {
            // Given
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { matchRemoteDataSource.getMatches() } returns errorResult

            // When
            val result = matchRepository.getMatches()

            // Then
            assertThat(result).isEqualTo(errorResult)
        }

    @Test
    fun `getUnseenMatches returns success when remote data source is successful`() =
        runTest {
            // Given
            val userDtoList = listOf(userDto)
            val successResult = MiraiLinkResult.Success(userDtoList)
            coEvery { matchRemoteDataSource.getUnseenMatches() } returns successResult

            // When
            val result = matchRepository.getUnseenMatches()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val users = (result as MiraiLinkResult.Success).data
            assertThat(users).hasSize(1)
            assertThat(users.first().id).isEqualTo(userDto.id)
        }

    @Test
    fun `getUnseenMatches returns error when remote data source fails`() =
        runTest {
            // Given
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { matchRemoteDataSource.getUnseenMatches() } returns errorResult

            // When
            val result = matchRepository.getUnseenMatches()

            // Then
            assertThat(result).isEqualTo(errorResult)
        }

    @Test
    fun `markMatchAsSeen returns success when remote data source is successful`() =
        runTest {
            // Given
            val matchIds = listOf("1", "2")
            val successResult = MiraiLinkResult.Success(Unit)
            coEvery { matchRemoteDataSource.markMatchAsSeen(matchIds) } returns successResult

            // When
            val result = matchRepository.markMatchAsSeen(matchIds)

            // Then
            assertThat(result).isEqualTo(successResult)
        }

    @Test
    fun `markMatchAsSeen returns error when remote data source fails`() =
        runTest {
            // Given
            val matchIds = listOf("1", "2")
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { matchRemoteDataSource.markMatchAsSeen(matchIds) } returns errorResult

            // When
            val result = matchRepository.markMatchAsSeen(matchIds)

            // Then
            assertThat(result).isEqualTo(errorResult)
        }
}
