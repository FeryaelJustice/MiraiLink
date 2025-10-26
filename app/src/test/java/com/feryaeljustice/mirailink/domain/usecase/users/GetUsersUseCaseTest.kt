package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UsersRepository
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
class GetUsersUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UsersRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun onBefore() {
        getUsersUseCase = GetUsersUseCase(repo)
    }

    @Test
    fun `when repository returns users, return success with user list`() = runTest {
        // Given
        val users = listOf(
            User(
                id = "1",
                username = "test1",
                nickname = "test1",
                email = "test1@test.com",
                phoneNumber = null,
                bio = null,
                gender = null,
                birthdate = null,
                photos = emptyList(),
                games = emptyList(),
                animes = emptyList()
            ),
            User(
                id = "2",
                username = "test2",
                nickname = "test2",
                email = "test2@test.com",
                phoneNumber = null,
                bio = null,
                gender = null,
                birthdate = null,
                photos = emptyList(),
                games = emptyList(),
                animes = emptyList()
            )
        )
        val successResult = MiraiLinkResult.Success(users)
        coEvery { repo.getUsers() } returns successResult

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(users, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to get users, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Could not fetch users")
        coEvery { repo.getUsers() } returns errorResult

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }
}