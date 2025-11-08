// Feryael Justice
// 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.auth.PasswordResetConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest
import com.feryaeljustice.mirailink.data.model.request.generic.ByIdRequest
import com.feryaeljustice.mirailink.data.model.request.generic.EmailRequest
import com.feryaeljustice.mirailink.data.model.request.verification.VerificationConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.verification.VerificationRequest
import com.feryaeljustice.mirailink.data.model.response.auth.AutologinResponse
import com.feryaeljustice.mirailink.data.model.response.auth.CheckIsVerifiedResponse
import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.data.model.response.auth.LogoutResponse
import com.feryaeljustice.mirailink.data.model.response.auth.RegisterResponse
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.photo.UploadPhotoResponse
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
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
class UserApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var userApiService: UserApiService
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

        userApiService =
            retrofit
                .create(UserApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun login_withValidCredentials_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = LoginResponse(token = "fake-token")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val loginRequest = LoginRequest("test@test.com", "testuser", "password")

            // When
            val response = userApiService.login(loginRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.token).isEqualTo("fake-token")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/login")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun register_withValidCredentials_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = RegisterResponse(message = "User registered successfully", token = "fake-token")
            val mockResponse =
                MockResponse()
                    .setResponseCode(201)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val registerRequest = RegisterRequest("testuser", "test@test.com", "password")

            // When
            val response = userApiService.register(registerRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("User registered successfully")
            assertThat(response.token).isEqualTo("fake-token")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/register")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun autologin_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = AutologinResponse(userId = "1234", message = "Autologin successful")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.autologin()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.userId).isEqualTo("1234")
            assertThat(response.message).isEqualTo("Autologin successful")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/autologin")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun logout_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = LogoutResponse(message = "Logout successful")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.logout()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Logout successful")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/logout")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun requestPasswordReset_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Password reset email sent")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val emailRequest = EmailRequest("test@test.com")

            // When
            val response = userApiService.requestPasswordReset(emailRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Password reset email sent")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/password/request-reset")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun confirmPasswordReset_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Password reset successfully")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val confirmRequest = PasswordResetConfirmRequest("test@test.com", "reset-token", "new-password")

            // When
            val response = userApiService.confirmPasswordReset(confirmRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Password reset successfully")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/password/confirm-reset")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun checkIsVerified_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = CheckIsVerifiedResponse(isVerified = true)
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.checkIsVerified()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.isVerified).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/verification/check")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun requestVerificationCode_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Verification code sent")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val verificationRequest = VerificationRequest("1234", "email")

            // When
            val response = userApiService.requestVerificationCode(verificationRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Verification code sent")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/verification/request")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun confirmVerificationCode_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Verification successful")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val verificationConfirmRequest = VerificationConfirmRequest("1234", "verification-code", "email")

            // When
            val response = userApiService.confirmVerificationCode(verificationConfirmRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Verification successful")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/auth/verification/confirm")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun getCurrentUser_returnsSuccess() =
        runBlocking {
            // Given
            val userDto =
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
                )
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(userDto))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.getCurrentUser()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.id).isEqualTo("1")
            assertThat(response.username).isEqualTo("testuser")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun deleteAccount_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = BasicResponse(message = "Account deleted successfully")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.deleteAccount()

            // Then
            assertThat(response).isNotNull()
            assertThat(response.message).isEqualTo("Account deleted successfully")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user")
            assertThat(request.method).isEqualTo("DELETE")
        }

    @Test
    fun deleteUserPhoto_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(204) // No content
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.deleteUserPhoto(0)

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user/photo/0")
            assertThat(request.method).isEqualTo("DELETE")
        }

    @Test
    fun getUserPhotos_returnsSuccess() =
        runBlocking {
            // Given
            val userPhotos = listOf(UserPhotoDto("1", "1", "url", 0))
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(userPhotos))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = userApiService.getUserPhotos("1")

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user/photos?userId=1")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun uploadUserPhoto_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = UploadPhotoResponse(url = "new-photo-url")
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)

            val photoRequestBody = "fake-image-data".toRequestBody("image/jpeg".toMediaTypeOrNull())
            val photoPart = MultipartBody.Part.createFormData("photo", "photo.jpg", photoRequestBody)
            val positionRequestBody = "0".toRequestBody("text/plain".toMediaTypeOrNull())

            // When
            val response = userApiService.uploadUserPhoto(photoPart, positionRequestBody)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.url).isEqualTo("new-photo-url")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user/photos")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun getUserById_returnsSuccess() =
        runBlocking {
            // Given
            val userDto =
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
                )
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(userDto))
            mockWebServer.enqueue(mockResponse)
            val byIdRequest = ByIdRequest("1")

            // When
            val response = userApiService.getUserById(byIdRequest)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.id).isEqualTo("1")
            assertThat(response.username).isEqualTo("testuser")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user/byId")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun updateProfile_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(204) // No content
            mockWebServer.enqueue(mockResponse)

            val nickname = "new_nickname".toRequestBody("text/plain".toMediaTypeOrNull())
            val bio = "new_bio".toRequestBody("text/plain".toMediaTypeOrNull())
            val gender = "Female".toRequestBody("text/plain".toMediaTypeOrNull())
            val birthdate = "1999-12-31".toRequestBody("text/plain".toMediaTypeOrNull())
            val animes = "[\"2\", \"3\"]".toRequestBody("application/json".toMediaTypeOrNull())
            val games = "[\"2\"]".toRequestBody("application/json".toMediaTypeOrNull())
            val reorderedPositions = "[1, 0]".toRequestBody("application/json".toMediaTypeOrNull())

            // When
            val response =
                userApiService.updateProfile(
                    nickname = nickname,
                    bio = bio,
                    gender = gender,
                    birthdate = birthdate,
                    animes = animes,
                    games = games,
                    reorderedPositions = reorderedPositions,
                )

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/user")
            assertThat(request.method).isEqualTo("PUT")
        }
}
