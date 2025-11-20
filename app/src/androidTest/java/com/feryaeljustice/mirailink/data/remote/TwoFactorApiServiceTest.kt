// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
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
class TwoFactorApiServiceTest : KoinTest {
    private val mockWebServer: MockWebServer by inject()
    private val twoFactorApiService: TwoFactorApiService by inject()

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
                single { get<Retrofit>().create(TwoFactorApiService::class.java) }
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
    fun getTwoFactorStatus_returnsSuccess() =
        runBlocking {
            // Given
            val response = TwoFactorStatusResponse(enabled = true)
            val mockResponse = MockResponse().setResponseCode(200).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)
            val requestBody = mapOf("userId" to "user123")

            // When
            val result = twoFactorApiService.getTwoFactorStatus(requestBody)

            // Then
            assertThat(result).isNotNull()
            assertThat(result.enabled).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/2fa/status")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun setupTwoFactor_returnsSuccess() =
        runBlocking {
            // Given
            val response = TwoFactorSetupResponse("otpauth_url", "base32_code", listOf("rec1", "rec2"))
            val mockResponse = MockResponse().setResponseCode(200).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)

            // When
            val result = twoFactorApiService.setupTwoFactor()

            // Then
            assertThat(result).isNotNull()
            assertThat(result.otpAuthUrl).isEqualTo("otpauth_url")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/2fa/setup")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun verifyTwoFactor_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(200)
            mockWebServer.enqueue(mockResponse)
            val requestBody = mapOf("token" to "123456")

            // When
            val response = twoFactorApiService.verifyTwoFactor(requestBody)

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/2fa/verify")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun disableTwoFactor_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(200)
            mockWebServer.enqueue(mockResponse)
            val requestBody = mapOf("code" to "123456")

            // When
            val response = twoFactorApiService.disableTwoFactor(requestBody)

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/2fa/disable")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun loginVerifyTwoFactorLastStep_returnsSuccess() =
        runBlocking {
            // Given
            val response = BasicResponse("Success")
            val mockResponse = MockResponse().setResponseCode(200).setBody(Json.encodeToString(response))
            mockWebServer.enqueue(mockResponse)
            val requestBody = mapOf("userId" to "user123", "code" to "123456")

            // When
            val result = twoFactorApiService.loginVerifyTwoFactorLastStep(requestBody)

            // Then
            assertThat(result).isNotNull()
            assertThat(result.message).isEqualTo("Success")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/2fa/login-verify")
            assertThat(request.method).isEqualTo("POST")
        }
}
