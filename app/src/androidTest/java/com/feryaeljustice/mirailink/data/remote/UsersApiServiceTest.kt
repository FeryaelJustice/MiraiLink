// Feryael Justice
// 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
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
class UsersApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var usersApiService: UsersApiService
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

        usersApiService =
            retrofit
                .create(UsersApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getUsers_returns() =
        runBlocking {
            // Given
            val listUserDto =
                listOf(
                    UserDto(
                        id = "1",
                        username = "testuser",
                        nickname = "Test User",
                        email = "test@test.com",
                        bio = "Bio",
                        gender = "Male",
                        birthdate = "2000-01-01",
                        animes = listOf(AnimeDto("1", "Anime 1", "url")),
                        games = listOf(GameDto("1", "Game 1", "url")),
                        photos = listOf(UserPhotoDto("1", "1", "url", 0)),
                    ),
                    UserDto(
                        id = "2",
                        username = "testuser2",
                        nickname = "Test User 2",
                        email = "test2@test.com",
                        bio = "Bio2",
                        gender = "Male",
                        birthdate = "2000-01-02",
                        animes = listOf(AnimeDto("2", "Anime 2", "url")),
                        games = listOf(GameDto("2", "Game 2", "url")),
                        photos = listOf(UserPhotoDto("2", "2", "url", 0)),
                    ),
                )

            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(listUserDto))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = usersApiService.getUsers()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.first().id).isEqualTo("1")
            assertThat(response.first().username).isEqualTo("testuser")
            assertThat(response[1].id).isEqualTo("2")
            assertThat(response[1].username).isEqualTo("testuser2")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/users")
            assertThat(request.method).isEqualTo("GET")
        }
}
