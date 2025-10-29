package com.feryaeljustice.mirailink.domain.usecase.onboarding

import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
@ExperimentalCoroutinesApi
class CheckOnboardingIsCompletedTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: OnboardingRepository

    private lateinit var checkOnboardingIsCompleted: CheckOnboardingIsCompleted

    @Before
    fun onBefore() {
        checkOnboardingIsCompleted = CheckOnboardingIsCompleted(repo)
    }

    @Test
    fun `when repository returns true, return success with true`() = runTest {
        // Given
        coEvery { repo.checkOnboardingIsCompleted() } returns MiraiLinkResult.Success(true)

        // When
        val result = checkOnboardingIsCompleted()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns false, return success with false`() = runTest {
        // Given
        coEvery { repo.checkOnboardingIsCompleted() } returns MiraiLinkResult.Success(false)

        // When
        val result = checkOnboardingIsCompleted()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertFalse((result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error checking onboarding status")
        coEvery { repo.checkOnboardingIsCompleted() } returns errorResult

        // When
        val result = checkOnboardingIsCompleted()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repo.checkOnboardingIsCompleted() } throws exception

        // When
        val result = checkOnboardingIsCompleted()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "CheckOnboardingIsCompleted error: ",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}