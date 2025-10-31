// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
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
class CatalogApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var catalogApiService: CatalogApiService
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        hiltRule.inject()

        val okHttp = OkHttpClient.Builder().build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(okHttp)
                .addConverterFactory(
                    json.asConverterFactory("application/json".toMediaType()),
                ).build()

        catalogApiService =
            retrofit
                .create(CatalogApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getAllAnimes_returnsSuccess() =
        runBlocking {
            // Given
            val animes = listOf(AnimeDto(id = "1", name = "Anime 1", imageUrl = "url"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(json.encodeToString(animes))

            mockWebServer.enqueue(mockResponse)

            // When
            val response = catalogApiService.getAllAnimes()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")

            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/catalog/animes")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun getAllGames_returnsSuccess() =
        runBlocking {
            // Given
            val games = listOf(GameDto(id = "1", name = "Game 1", imageUrl = "url"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(json.encodeToString(games))

            mockWebServer.enqueue(mockResponse)

            // When
            val response = catalogApiService.getAllGames()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")

            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/catalog/games")
            assertThat(request.method).isEqualTo("GET")
        }
}
