package com.feryaeljustice.mirailink.ui.screens.ai.chat

import app.cash.turbine.test
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/** State-machine and recovery tests for [AiChatViewModel]. */
@OptIn(ExperimentalCoroutinesApi::class)
class AiChatViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val generateContentUseCase = mockk<GenerateContentUseCase>()

    /** Verifies the complete idle, loading and success state sequence. */
    @Test
    fun `send message emits loading and success`() = runTest(mainCoroutineRule.scheduler) {
        // Given
        coEvery { generateContentUseCase("Hello") } returns MiraiLinkResult.Success("Hi")
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AiChatUiState.Idle)

            // When
            viewModel.sendMessage("Hello")

            // Then
            assertThat(awaitItem()).isEqualTo(AiChatUiState.Loading)
            assertThat(awaitItem()).isEqualTo(AiChatUiState.Success("Hi"))
            coVerify(exactly = 1) { generateContentUseCase("Hello") }
        }
    }

    /** Verifies that a domain error is converted to actionable presentation data. */
    @Test
    fun `send message emits mapped error`() = runTest(mainCoroutineRule.scheduler) {
        // Given
        coEvery { generateContentUseCase(any()) } returns MiraiLinkResult.Error(DataError.Network.NO_CONNECTION)
        val viewModel = createViewModel()

        // When
        viewModel.sendMessage("Hello")
        mainCoroutineRule.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value)
            .isEqualTo(AiChatUiState.Error(DataError.Network.NO_CONNECTION.toUiError()))
    }

    /** Verifies that the UI recovery action repeats the exact failed prompt. */
    @Test
    fun `error action retries last prompt`() = runTest(mainCoroutineRule.scheduler) {
        // Given
        coEvery { generateContentUseCase("Retry me") } returnsMany
            listOf(
                MiraiLinkResult.Error(DataError.Network.NO_CONNECTION),
                MiraiLinkResult.Success("Recovered"),
            )
        val viewModel = createViewModel()
        viewModel.sendMessage("Retry me")
        mainCoroutineRule.scheduler.advanceUntilIdle()

        // When
        viewModel.performErrorAction()
        mainCoroutineRule.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(AiChatUiState.Success("Recovered"))
        coVerify(exactly = 2) { generateContentUseCase("Retry me") }
    }

    /** Creates the system under test with the shared deterministic dispatcher. */
    private fun createViewModel() =
        AiChatViewModel(
            generateContentUseCase = generateContentUseCase,
            ioDispatcher = mainCoroutineRule.testDispatcher,
        )
}
