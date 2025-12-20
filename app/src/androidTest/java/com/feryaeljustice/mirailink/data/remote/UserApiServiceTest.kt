package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest
import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.data.model.response.auth.RegisterResponse
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
class UserApiServiceTest : KoinTest {
    private val mockWebServer: MockWebServer by inject()
    private val userApiService: UserApiService by inject()

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
                    single { get<Retrofit>().create(UserApiService::class.java) }
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
    fun login_returnsSuccess() =
        runBlocking {
            // Given
            val response = LoginResponse("token")
            val mockResponse =
                MockResponse().setResponseCode(200).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)
            val loginRequest = LoginRequest("test@test.com", "testuser", "password")

            // When
            val result = userApiService.login(loginRequest)

            // Then
            assertThat(result).isNotNull()
            assertThat(result.token).isEqualTo("token")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/users/login")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun register_returnsSuccess() =
        runBlocking {
            // Given
            val response = RegisterResponse("User created", "token")
            val mockResponse =
                MockResponse().setResponseCode(201).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)
            val registerRequest = RegisterRequest("testuser", "test@test.com", "password")

            // When
            val result = userApiService.register(registerRequest)

            // Then
            assertThat(result).isNotNull()
            assertThat(result.token).isEqualTo("token")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/users/register")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun getCurrentUser_returnsSuccess() =
        runBlocking {
            // Given
            val user =
                UserDto(
                    "1",
                    "testuser",
                    "Test User",
                    "test@test.com",
                    null,
                    null,
                    null,
                    null,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                )
            val mockResponse =
                MockResponse().setResponseCode(200).setBody(Json.encodeToString(user))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.getCurrentUser()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/users/me")
            assertThat(request.method).isEqualTo("GET")
        }
}
