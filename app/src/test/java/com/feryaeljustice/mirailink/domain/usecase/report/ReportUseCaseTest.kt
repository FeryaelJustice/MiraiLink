package com.feryaeljustice.mirailink.domain.usecase.report

import com.feryaeljustice.mirailink.domain.repository.ReportRepository
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
class ReportUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: ReportRepository

    private lateinit var reportUseCase: ReportUseCase

    @Before
    fun onBefore() {
        reportUseCase = ReportUseCase(repo)
    }

    @Test
    fun `when repository reports user successfully, return success`() = runTest {
        // Given
        coEvery { repo.reportUser(any(), any()) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = reportUseCase("reportedUser", "reason")

        // Then
        assertTrue(result is MiraiLinkResult.Success)
    }

    @Test
    fun `when repository fails to report user, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Could not report user")
        coEvery { repo.reportUser(any(), any()) } returns errorResult

        // When
        val result = reportUseCase("reportedUser", "reason")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repo.reportUser(any(), any()) } throws exception

        // When
        val result = reportUseCase("reportedUser", "reason")

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("ReportUseCase error", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}