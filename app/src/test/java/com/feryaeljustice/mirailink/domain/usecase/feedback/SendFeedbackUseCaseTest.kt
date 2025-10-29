/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.feedback

import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
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

@ExperimentalCoroutinesApi
class SendFeedbackUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: FeedbackRepository

    private lateinit var sendFeedbackUseCase: SendFeedbackUseCase

    @Before
    fun onBefore() {
        sendFeedbackUseCase = SendFeedbackUseCase(repository)
    }

    @Test
    fun `when repository sends feedback successfully, return success`() = runTest {
        // Given
        val feedback = "This is a test feedback"
        coEvery { repository.sendFeedback(feedback) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = sendFeedbackUseCase(feedback)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
    }

    @Test
    fun `when repository fails to send feedback, return error`() = runTest {
        // Given
        val feedback = "This is a test feedback"
        val errorResult = MiraiLinkResult.Error("Se produjo un error al enviar el feedback")
        coEvery { repository.sendFeedback(feedback) } returns errorResult

        // When
        val result = sendFeedbackUseCase(feedback)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val feedback = "This is a test feedback"
        val exception = RuntimeException("Network error")
        coEvery { repository.sendFeedback(feedback) } throws exception

        // When
        val result = sendFeedbackUseCase(feedback)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "Se produjo un error al enviar el feedback",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}