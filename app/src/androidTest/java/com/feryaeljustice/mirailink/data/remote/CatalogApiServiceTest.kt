package com.feryaeljustice.mirailink.data.remote

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseApiUrl
import com.feryaeljustice.mirailink.di.koin.cryptoModule
import com.feryaeljustice.mirailink.di.koin.dataModule
import com.feryaeljustice.mirailink.di.koin.dataStoreModule
import com.feryaeljustice.mirailink.di.koin.dispatchersModule
import com.feryaeljustice.mirailink.di.koin.networkModule
import com.feryaeljustice.mirailink.di.koin.repositoryModule
import com.feryaeljustice.mirailink.di.koin.serializationModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class CatalogApiServiceTest : KoinTest {
    private val catalogApiService: CatalogApiService by inject()
    private val mockWebServer: MockWebServer by inject()
    private val json: Json by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(
                networkModule,
                dataModule,
                dataStoreModule,
                dispatchersModule,
                repositoryModule,
                serializationModule,
                cryptoModule,
                module {
                    single { MockWebServer() }
                    single(qualifier = BaseApiUrl) { get<MockWebServer>().url("/").toString() }
                },
            )
        }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        stopKoin()
    }

    @Test
    fun getAllAnimes_returnsSuccess() =
        runBlocking {
            // Given
            val animeList = listOf(AnimeDto("1", "Naruto", "url"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(json.encodeToString(animeList))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = catalogApiService.getAllAnimes()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("catalog/animes")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun getAllGames_returnsSuccess() =
        runBlocking {
            // Given
            val gamesList = listOf(AnimeDto("1", "League of Legends", "url"))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(json.encodeToString(gamesList))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = catalogApiService.getAllGames()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("catalog/games")
            assertThat(request.method).isEqualTo("GET")
        }
}
