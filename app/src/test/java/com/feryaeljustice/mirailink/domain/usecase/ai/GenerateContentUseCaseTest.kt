package com.feryaeljustice.mirailink.domain.usecase.ai

import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.repository.AiRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Unit tests for AI result wrapping, failure classification and cancellation. */
class GenerateContentUseCaseTest {
    private val repository = mockk<AiRepository>()
    private val useCase = GenerateContentUseCase(repository)

    /** Verifies the successful domain result and exact prompt delegation. */
    @Test
    fun `invoke returns generated content`() = runTest {
        // Given
        coEvery { repository.generateContent("Write a greeting") } returns "Hello"

        // When
        val result = useCase("Write a greeting")

        // Then
        assertThat(result).isEqualTo(MiraiLinkResult.Success("Hello"))
        coVerify(exactly = 1) { repository.generateContent("Write a greeting") }
    }

    /** Verifies that infrastructure exceptions are converted into the public domain error. */
    @Test
    fun `invoke maps unexpected exception to unknown error`() = runTest {
        // Given
        coEvery { repository.generateContent(any()) } throws IllegalStateException("SDK failure")

        // When
        val result = useCase("Prompt")

        // Then
        assertThat(result).isEqualTo(MiraiLinkResult.Error(UnknownError))
    }

    /** Verifies structured concurrency by never swallowing coroutine cancellation. */
    @Test
    fun `invoke rethrows cancellation exception`() = runTest {
        // Given
        val cancellation = CancellationException("cancelled")
        coEvery { repository.generateContent(any()) } throws cancellation

        // When
        val thrown = runCatching { useCase("Prompt") }.exceptionOrNull()

        // Then
        assertThat(thrown).isSameInstanceAs(cancellation)
    }
}
