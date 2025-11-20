// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest
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
class FeedbackApiServiceTest : KoinTest {
    private val mockWebServer: MockWebServer by inject()
    private val feedbackApiService: FeedbackApiService by inject()

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
                single { get<Retrofit>().create(FeedbackApiService::class.java) }
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
    fun sendFeedback_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(200)
            mockWebServer.enqueue(mockResponse)
            val feedbackRequest = SendFeedbackRequest("This is a test feedback")

            // When
            val response = feedbackApiService.sendFeeback(feedbackRequest)

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/feedback")
            assertThat(request.method).isEqualTo("POST")
        }
}
