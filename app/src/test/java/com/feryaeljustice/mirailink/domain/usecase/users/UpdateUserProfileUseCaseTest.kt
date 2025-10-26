package com.feryaeljustice.mirailink.domain.usecase.users

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
class UpdateUserProfileUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var updateUserProfileUseCase: UpdateUserProfileUseCase

    @Before
    fun onBefore() {
        updateUserProfileUseCase = UpdateUserProfileUseCase(repo)
    }

    @Test
    fun `when repository updates user profile successfully, return success`() = runTest {
        // Given
        coEvery {
            repo.updateProfile(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns MiraiLinkResult.Success(Unit)

        // When
        val result = updateUserProfileUseCase(
            "nickname",
            "bio",
            "gender",
            "birthdate",
            "animes",
            "games",
            emptyList(),
            emptyList()
        )

        // Then
        assertTrue(result is MiraiLinkResult.Success)
    }

    @Test
    fun `when repository fails to update user profile, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Could not update profile")
        coEvery {
            repo.updateProfile(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns errorResult

        // When
        val result = updateUserProfileUseCase(
            "nickname",
            "bio",
            "gender",
            "birthdate",
            "animes",
            "games",
            emptyList(),
            emptyList()
        )

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }
}