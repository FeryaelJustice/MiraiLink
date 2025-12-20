package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.google.common.truth.Truth.assertThat
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
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@RunWith(AndroidJUnit4::class)
class MatchApiServiceTest : KoinTest {
    private val mockWebServer: MockWebServer by inject()
    private val matchApiService: MatchApiService by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { MockWebServer() }
                    single {
                        val client = OkHttpClient.Builder().build()
                        val json = Json { ignoreUnknownKeys = true }
                        Retrofit
                            .Builder()
                            .baseUrl(get<MockWebServer>().url("/"))
                            .client(client)
                            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                            .build()
                    }
                    single { get<Retrofit>().create(MatchApiService::class.java) }
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
    fun getMatches_returnsSuccess() =
        runBlocking {
            // Given
            val userList = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
            val mockResponse =
                MockResponse().setResponseCode(200).setBody(Json.encodeToString(userList))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = matchApiService.getMatches()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/matches")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun getUnseenMatches_returnsSuccess() =
        runBlocking {
            // Given
            val userList = listOf(UserDto(id = "2", username = "unseenuser", nickname = "Unseen User"))
            val mockResponse =
                MockResponse().setResponseCode(200).setBody(Json.encodeToString(userList))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = matchApiService.getUnseenMatches()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("2")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/matches/unseen")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun markMatchAsSeen_returnsSuccess() =
        runBlocking {
            // Given
            val matchIds = listOf("1", "2")
            val requestBody = MarkMatchAsSeenRequest(matchIds)
            val mockResponse = MockResponse().setResponseCode(200).setBody(Json.encodeToString(BasicResponse("Success")))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = matchApiService.markMatchAsSeen(requestBody)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Success")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/matches/seen")
            assertThat(request.method).isEqualTo("POST")
        }
}
