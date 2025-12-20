package com.feryaeljustice.mirailink.data.datasource

import android.content.Context
import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest
import com.feryaeljustice.mirailink.data.model.request.generic.ByIdRequest
import com.feryaeljustice.mirailink.data.model.response.auth.AutologinResponse
import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.data.model.response.auth.LogoutResponse
import com.feryaeljustice.mirailink.data.model.response.auth.RegisterResponse
import com.feryaeljustice.mirailink.data.remote.UserApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UserRemoteDataSourceTest : UnitTest() {
    private val userApiService: UserApiService by inject()
    private val context: Context by inject()
    private val userRemoteDataSource: UserRemoteDataSource by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<UserApiService>() }
                    single { mockk<Context>(relaxed = true) }
                    single { UserRemoteDataSource(get(), get()) }
                },
            )
        }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `autologin should return userId on success`() =
        runTest {
            // Given
            val response = AutologinResponse("user123", "Success")
            coEvery { userApiService.autologin() } returns response

            // When
            val result = userRemoteDataSource.autologin()

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals("user123", (result as MiraiLinkResult.Success).data)
            coVerify { userApiService.autologin() }
        }

    @Test
    fun `login should return token on success`() =
        runTest {
            // Given
            val request = LoginRequest("test@test.com", "testuser", "password")
            val response = LoginResponse("token123")
            coEvery { userApiService.login(request) } returns response

            // When
            val result = userRemoteDataSource.login(request.email, request.username, request.password)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals("token123", (result as MiraiLinkResult.Success).data)
            coVerify { userApiService.login(request) }
        }

    @Test
    fun `logout should return true on success`() =
        runTest {
            // Given
            coEvery { userApiService.logout() } returns LogoutResponse("Success")

            // When
            val result = userRemoteDataSource.logout()

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(true, (result as MiraiLinkResult.Success).data)
            coVerify { userApiService.logout() }
        }

    @Test
    fun `register should return token on success`() =
        runTest {
            // Given
            val request = RegisterRequest("testuser", "test@test.com", "password")
            val response = RegisterResponse("User created", "token456")
            coEvery { userApiService.register(request) } returns response

            // When
            val result = userRemoteDataSource.register(request.username, request.email, request.password)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals("token456", (result as MiraiLinkResult.Success).data)
            coVerify { userApiService.register(request) }
        }

    @Test
    fun `getCurrentUser should return user and photos on success`() =
        runTest {
            // Given
            val user = UserDto("1", "testuser", "Test User")
            val photos = listOf(UserPhotoDto("p1", "1", "url", 1))
            coEvery { userApiService.getCurrentUser() } returns user
            coEvery { userApiService.getUserPhotos(user.id) } returns photos

            // When
            val result = userRemoteDataSource.getCurrentUser()

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(user to photos, (result as MiraiLinkResult.Success).data)
            coVerify { userApiService.getCurrentUser() }
            coVerify { userApiService.getUserPhotos(user.id) }
        }

    @Test
    fun `getUserById should return user and photos on success`() =
        runTest {
            // Given
            val userId = "1"
            val request = ByIdRequest(userId)
            val user = UserDto(userId, "testuser", "Test User")
            val photos = listOf(UserPhotoDto("p1", userId, "url", 1))
            coEvery { userApiService.getUserById(request) } returns user
            coEvery { userApiService.getUserPhotos(userId) } returns photos

            // When
            val result = userRemoteDataSource.getUserById(userId)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(user to photos, (result as MiraiLinkResult.Success).data)
            coVerify { userApiService.getUserById(request) }
            coVerify { userApiService.getUserPhotos(userId) }
        }
}
