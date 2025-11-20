// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.di.koin.Qualifiers
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SwipeRepositoryImplTest : UnitTest() {
    private val swipeRepository: SwipeRepositoryImpl by inject()
    private val swipeRemoteDataSource: SwipeRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<SwipeRemoteDataSource>() }
                single(Qualifiers.BaseUrl) { "http://localhost:8080" }
                single { SwipeRepositoryImpl(get(), get(Qualifiers.BaseUrl)) }
            },
        )
    }

    private val userDto =
        UserDto(
            id = "1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            photos = emptyList(),
            animes = emptyList(),
            games = emptyList(),
        )

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getFeed returns success when remote data source is successful`() =
        runTest {
            // Given
            val userDtoList = listOf(userDto)
            val successResult = MiraiLinkResult.Success(userDtoList)
            coEvery { swipeRemoteDataSource.getFeed() } returns successResult

            // When
            val result = swipeRepository.getFeed()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val users = (result as MiraiLinkResult.Success).data
            assertThat(users).hasSize(1)
            assertThat(users.first().id).isEqualTo(userDto.id)
        }

    @Test
    fun `getFeed returns error when remote data source fails`() =
        runTest {
            // Given
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { swipeRemoteDataSource.getFeed() } returns errorResult

            // When
            val result = swipeRepository.getFeed()

            // Then
            assertThat(result).isEqualTo(errorResult)
        }

    @Test
    fun `likeUser returns success when remote data source is successful`() =
        runTest {
            // Given
            val userId = "2"
            val successResult = MiraiLinkResult.Success(true)
            coEvery { swipeRemoteDataSource.likeUser(userId) } returns successResult

            // When
            val result = swipeRepository.likeUser(userId)

            // Then
            assertThat(result).isEqualTo(successResult)
        }

    @Test
    fun `likeUser returns error when remote data source fails`() =
        runTest {
            // Given
            val userId = "2"
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { swipeRemoteDataSource.likeUser(userId) } returns errorResult

            // When
            val result = swipeRepository.likeUser(userId)

            // Then
            assertThat(result).isEqualTo(errorResult)
        }

    @Test
    fun `dislikeUser returns success when remote data source is successful`() =
        runTest {
            // Given
            val userId = "2"
            val successResult = MiraiLinkResult.Success(Unit)
            coEvery { swipeRemoteDataSource.dislikeUser(userId) } returns successResult

            // When
            val result = swipeRepository.dislikeUser(userId)

            // Then
            assertThat(result).isEqualTo(successResult)
        }

    @Test
    fun `dislikeUser returns error when remote data source fails`() =
        runTest {
            // Given
            val userId = "2"
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { swipeRemoteDataSource.dislikeUser(userId) } returns errorResult

            // When
            val result = swipeRepository.dislikeUser(userId)

            // Then
            assertThat(result).isEqualTo(errorResult)
        }
}
