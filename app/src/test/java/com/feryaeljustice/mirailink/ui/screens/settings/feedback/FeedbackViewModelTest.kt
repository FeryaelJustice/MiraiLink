// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.settings.feedback

import com.feryaeljustice.mirailink.domain.usecase.feedback.SendFeedbackUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class FeedbackViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val sendFeedbackUseCase: SendFeedbackUseCase by inject()

    private lateinit var viewModel: FeedbackViewModel

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<SendFeedbackUseCase>() }
            },
        )
    }

    @Before
    fun setUp() {
        viewModel = FeedbackViewModel(sendFeedbackUseCase, mainCoroutineRule.testDispatcher)
    }

    @Test
    fun `send feedback success`() =
        runTest {
            val feedback = "This is a test feedback."
            coEvery { sendFeedbackUseCase.invoke(feedback) } returns
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
            coEvery { sendFeedbackUseCase.invoke(feedback) } returns
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
