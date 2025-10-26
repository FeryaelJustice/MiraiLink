package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Feryael Justice
 * @since 18/10/2024
 */
@ExperimentalCoroutinesApi
class GetUserByIdUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @Before
    fun onBefore() {
        getUserByIdUseCase = GetUserByIdUseCase(repo)
    }

    @Test
    fun `when repository returns user by id, return success with user`() = runTest {
        // Given
        val user = User(
            id = "1",
            username = "test",
            nickname = "test",
            email = "test@test.com",
            phoneNumber = null,
            bio = null,
            gender = null,
            birthdate = null,
            photos = emptyList(),
            games = emptyList(),
            animes = emptyList()
        )
        val successResult = MiraiLinkResult.Success(user)
        coEvery { repo.getUserById("1") } returns successResult

        // When
        val result = getUserByIdUseCase("1")

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(user, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to get user by id, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("User not found")
        coEvery { repo.getUserById("1") } returns errorResult

        // When
        val result = getUserByIdUseCase("1")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }
}