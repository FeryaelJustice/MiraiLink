package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.swipe.SwipeResponse
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SwipeRemoteDataSourceTest : UnitTest() {
    private val swipeApiService: SwipeApiService by inject()
    private val swipeRemoteDataSource: SwipeRemoteDataSource by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<SwipeApiService>() }
                    single { SwipeRemoteDataSource(get()) }
                },
            )
        }

    @Before
    override fun setUp() {
        super.setUp()

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `getFeed should return list of users on success`() =
        runTest {
            // Given
            val userList = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
            coEvery { swipeApiService.getFeed() } returns userList

            // When
            val result = swipeRemoteDataSource.getFeed()

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(userList, (result as MiraiLinkResult.Success).data)
            coVerify { swipeApiService.getFeed() }
        }

    @Test
    fun `likeUser should return true on match`() =
        runTest {
            // Given
            val toUserId = "2"
            val request = SwipeRequest(toUserId)
            val response = SwipeResponse("It's a match!", match = true)
            coEvery { swipeApiService.likeUser(request) } returns response

            // When
            val result = swipeRemoteDataSource.likeUser(toUserId)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(true, (result as MiraiLinkResult.Success).data)
            coVerify { swipeApiService.likeUser(request) }
        }

    @Test
    fun `likeUser should return false on no match`() =
        runTest {
            // Given
            val toUserId = "3"
            val request = SwipeRequest(toUserId)
            val response = SwipeResponse("Liked", match = false)
            coEvery { swipeApiService.likeUser(request) } returns response

            // When
            val result = swipeRemoteDataSource.likeUser(toUserId)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(false, (result as MiraiLinkResult.Success).data)
            coVerify { swipeApiService.likeUser(request) }
        }

    @Test
    fun `dislikeUser should return success`() =
        runTest {
            // Given
            val toUserId = "4"
            val request = SwipeRequest(toUserId)
            val response = BasicResponse("Disliked")
            coEvery { swipeApiService.dislikeUser(request) } returns response

            // When
            val result = swipeRemoteDataSource.dislikeUser(toUserId)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            coVerify { swipeApiService.dislikeUser(request) }
        }
}
