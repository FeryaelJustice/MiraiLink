package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.GeminiDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Unit contract tests for the AI repository delegation boundary. */
class AiRepositoryImplTest {
    private val dataSource = mockk<GeminiDataSource>()
    private val repository = AiRepositoryImpl(dataSource)

    /** Verifies that the repository returns the exact generated content without altering it. */
    @Test
    fun `generate content delegates prompt and returns data source response`() = runTest {
        // Given
        coEvery { dataSource.generateContent("Hello") } returns "Generated answer"

        // When
        val result = repository.generateContent("Hello")

        // Then
        assertThat(result).isEqualTo("Generated answer")
        coVerify(exactly = 1) { dataSource.generateContent("Hello") }
    }

    /** Verifies that unexpected data source failures are not hidden by this thin repository. */
    @Test
    fun `generate content propagates data source failure`() = runTest {
        // Given
        val failure = IllegalStateException("generation failed")
        coEvery { dataSource.generateContent(any()) } throws failure

        // When
        val thrown = runCatching { repository.generateContent("Hello") }.exceptionOrNull()

        // Then
        assertThat(thrown).isSameInstanceAs(failure)
    }
}
