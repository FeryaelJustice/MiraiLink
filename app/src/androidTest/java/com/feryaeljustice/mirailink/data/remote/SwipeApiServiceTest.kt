// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.swipe.SwipeResponse
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class SwipeApiServiceTest : KoinTest {
    private val mockWebServer: MockWebServer by inject()
    private val swipeApiService: SwipeApiService by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { MockWebServer() }
                single {
                    val client = OkHttpClient.Builder().build()
                    val json = Json { ignoreUnknownKeys = true }
                    Retrofit.Builder()
                        .baseUrl(get<MockWebServer>().url("/"))
                        .client(client)
                        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                        .build()
                }
                single { get<Retrofit>().create(SwipeApiService::class.java) }
            },
        )
    }

    @Before
    fun setUp() {
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun likeUser_returnsSuccess() =
        runBlocking {
            // Given
            val response = SwipeResponse("It's a match!", match = true)
            val mockResponse = MockResponse().setResponseCode(200).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)
            val swipeRequest = SwipeRequest("user2")

            // When
            val result = swipeApiService.likeUser(swipeRequest)

            // Then
            assertThat(result).isNotNull()
            assertThat(result.match).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/swipe/like")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun dislikeUser_returnsSuccess() =
        runBlocking {
            // Given
            val response = BasicResponse("Disliked")
            val mockResponse = MockResponse().setResponseCode(200).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)
            val swipeRequest = SwipeRequest("user2")

            // When
            val result = swipeApiService.dislikeUser(swipeRequest)

            // Then
            assertThat(result).isNotNull()
            assertThat(result.message).isEqualTo("Disliked")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/swipe/dislike")
            assertThat(request.method).isEqualTo("POST")
        }
}
