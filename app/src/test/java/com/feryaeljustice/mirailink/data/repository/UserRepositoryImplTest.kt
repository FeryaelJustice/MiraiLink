package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.di.koin.Qualifiers
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UserRepositoryImplTest : UnitTest() {
    private val userRepository: UserRepositoryImpl by inject()
    private val userRemoteDataSource: UserRemoteDataSource by inject()
    private val sessionManager: SessionManager by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<UserRemoteDataSource>() }
                    single { mockk<SessionManager>(relaxed = true) }
                    single(Qualifiers.BaseUrl) { "http://localhost:8080" }
                    single { UserRepositoryImpl(get(), get(), get(Qualifiers.BaseUrl)) }
                },
            )
        }

    private val userDto =
        UserDto(
            id = "1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            photos = emptyList(),
            animes = emptyList(),
            games = emptyList(),
        )

    private val userPhotoDto =
        UserPhotoDto(
            id = "photo1",
            userId = "1",
            url = "/path/to/photo.jpg",
            position = 1,
        )

    private val user =
        User(
            id = "1",
            username = "testuser",
            nickname = "Test User",
            email = "test@example.com",
            phoneNumber = null,
            bio = null,
            gender = null,
            birthdate = null,
            photos =
                listOf(
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto(
                        userId = "1",
                        url = "http://localhost:8080/path/to/photo.jpg",
                        position = 1,
                    ),
                ),
            games = emptyList(),
            animes = emptyList(),
        )

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getCurrentUser returns success when remote data source is successful`() =
        runTest {
            // Given
            val successResult = MiraiLinkResult.Success(userDto to listOf(userPhotoDto))
            coEvery { userRemoteDataSource.getCurrentUser() } returns successResult

            // When
            val result = userRepository.getCurrentUser()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val successData = (result as MiraiLinkResult.Success).data
            assertThat(successData.id).isEqualTo(user.id)
            assertThat(successData.username).isEqualTo(user.username)
            assertThat(successData.photos.first().url).endsWith(userPhotoDto.url)
        }

    @Test
    fun `getCurrentUser returns error when remote data source fails`() =
        runTest {
            // Given
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { userRemoteDataSource.getCurrentUser() } returns errorResult

            // When
            val result = userRepository.getCurrentUser()

            // Then
            assertThat(result).isEqualTo(errorResult)
        }

    @Test
    fun `getUserById returns success when remote data source is successful`() =
        runTest {
            // Given
            val userId = "1"
            val successResult = MiraiLinkResult.Success(userDto to listOf(userPhotoDto))
            coEvery { userRemoteDataSource.getUserById(userId) } returns successResult

            // When
            val result = userRepository.getUserById(userId)

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val successData = (result as MiraiLinkResult.Success).data
            assertThat(successData.id).isEqualTo(user.id)
            assertThat(successData.username).isEqualTo(user.username)
            assertThat(successData.photos.first().url).endsWith(userPhotoDto.url)
        }

    @Test
    fun `getUserById returns error when remote data source fails`() =
        runTest {
            // Given
            val userId = "1"
            val errorResult = MiraiLinkResult.Error("An error occurred")
            coEvery { userRemoteDataSource.getUserById(userId) } returns errorResult

            // When
            val result = userRepository.getUserById(userId)

            // Then
            assertThat(result).isEqualTo(errorResult)
        }
}
