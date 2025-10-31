// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
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
class MatchApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var matchApiService: MatchApiService
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
                .addConverterFactory(
                    json.asConverterFactory("application/json".toMediaType()),
                ).build()

        matchApiService = retrofit.create(MatchApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getMatches_returnsSuccess() =
        runBlocking {
            // Given
            val users = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(users))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = matchApiService.getMatches()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/match")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun getUnseenMatches_returnsSuccess() =
        runBlocking {
            // Given
            val users = listOf(UserDto(id = "2", username = "testuser2", nickname = "Test User 2"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(users))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = matchApiService.getUnseenMatches()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("2")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/match/unseen")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun markMatchAsSeen_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Matches marked as seen")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val requestBody = MarkMatchAsSeenRequest(listOf("1", "2"))

            // When
            val response = matchApiService.markMatchAsSeen(requestBody)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Matches marked as seen")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/match/mark-seen")
            assertThat(request.method).isEqualTo("POST")
        }
}
