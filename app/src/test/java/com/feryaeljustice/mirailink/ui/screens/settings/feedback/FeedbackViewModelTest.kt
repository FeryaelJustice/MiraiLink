// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.settings.feedback

import com.feryaeljustice.mirailink.domain.usecase.feedback.SendFeedbackUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedbackViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FeedbackViewModel
    private val sendFeedbackUseCase: Lazy<SendFeedbackUseCase> = mockk()

    @Before
    fun setUp() {
        viewModel = FeedbackViewModel(sendFeedbackUseCase, mainCoroutineRule.testDispatcher)
    }

    @Test
    fun `send feedback success`() =
        runTest {
            val feedback = "This is a test feedback."
            coEvery { sendFeedbackUseCase.get().invoke(feedback) } returns
                MiraiLinkResult.Success(
                    Unit,
                )

            viewModel.updateFeedback(feedback)

            var onFinishCalled = false
            viewModel.sendFeedback { onFinishCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(onFinishCalled)
            assert(!viewModel.uiState.value.loading)
            assert(viewModel.uiState.value.error == null)
            assert(
                viewModel.uiState.value.feedback
                    .isEmpty(),
            )
        }

    @Test
    fun `send feedback error`() =
        runTest {
            val feedback = "This is a test feedback."
            val errorMessage = "Error message"
            coEvery { sendFeedbackUseCase.get().invoke(feedback) } returns
                MiraiLinkResult.Error(
                    errorMessage,
                )

            viewModel.updateFeedback(feedback)

            var onFinishCalled = false
            viewModel.sendFeedback { onFinishCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!onFinishCalled)
            assert(!viewModel.uiState.value.loading)
            assert(viewModel.uiState.value.error == errorMessage)
        }
}
