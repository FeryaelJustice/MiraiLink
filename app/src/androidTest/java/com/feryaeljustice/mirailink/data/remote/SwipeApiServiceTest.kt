// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.swipe.SwipeResponse
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SwipeApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var swipeApiService: SwipeApiService
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        hiltRule.inject()

        val okHttp =
            OkHttpClient
                .Builder()
                .build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(okHttp)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

        swipeApiService =
            retrofit
                .create(SwipeApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getFeed_returnsSuccess() =
        runBlocking {
            // Given
            val users = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(users))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = swipeApiService.getFeed()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/swipe/feed?limit=10&offset=0")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun likeUser_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = SwipeResponse(message = "Like successful", match = true)
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val swipeRequest = SwipeRequest(toUserId = "2")

            // When
            val response = swipeApiService.likeUser(swipeRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.match).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/swipe/like")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun dislikeUser_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Dislike successful")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val swipeRequest = SwipeRequest(toUserId = "2")

            // When
            val response = swipeApiService.dislikeUser(swipeRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Dislike successful")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/swipe/dislike")
            assertThat(request.method).isEqualTo("POST")
        }
}
