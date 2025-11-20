// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.UserDto
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
class UsersApiServiceTest : KoinTest {
    private val mockWebServer: MockWebServer by inject()
    private val usersApiService: UsersApiService by inject()

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
                single { get<Retrofit>().create(UsersApiService::class.java) }
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
    fun getUsers_returnsSuccess() =
        runBlocking {
            // Given
            val userList = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
            val mockResponse =
                MockResponse().setResponseCode(200).setBody(Json.encodeToString(userList))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = usersApiService.getUsers()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/users")
            assertThat(request.method).isEqualTo("GET")
        }
}
