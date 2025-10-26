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
class GetCurrentUserUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Before
    fun onBefore() {
        getCurrentUserUseCase = GetCurrentUserUseCase(repo)
    }

    @Test
    fun `when repository returns current user, return success with user`() = runTest {
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
        coEvery { repo.getCurrentUser() } returns successResult

        // When
        val result = getCurrentUserUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(user, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to get current user, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("User not found")
        coEvery { repo.getCurrentUser() } returns errorResult

        // When
        val result = getCurrentUserUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }
}