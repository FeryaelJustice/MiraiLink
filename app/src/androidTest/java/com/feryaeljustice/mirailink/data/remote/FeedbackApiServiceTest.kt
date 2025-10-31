// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest
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
class FeedbackApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var feedbackApiService: FeedbackApiService
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

        feedbackApiService =
            retrofit
                .create(FeedbackApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun sendFeedback_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(204) // No content
            mockWebServer.enqueue(mockResponse)
            val sendFeedbackRequest = SendFeedbackRequest(feedback = "This is a test feedback")

            // When
            val response = feedbackApiService.sendFeeback(sendFeedbackRequest)

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/feedback")
            assertThat(request.method).isEqualTo("POST")
        }
}
