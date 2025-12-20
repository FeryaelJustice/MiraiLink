package com.feryaeljustice.mirailink.ui.screens.auth.recover

import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase
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
class RecoverPasswordViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val requestPasswordResetUseCase: RequestPasswordResetUseCase by inject()
    private val confirmPasswordResetUseCase: ConfirmPasswordResetUseCase by inject()

    private lateinit var viewModel: RecoverPasswordViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<RequestPasswordResetUseCase>() }
                    single { mockk<ConfirmPasswordResetUseCase>() }
                },
            )
        }

    @Before
    fun setUp() {
        viewModel =
            RecoverPasswordViewModel(
                requestPasswordResetUseCase,
                confirmPasswordResetUseCase,
                mainCoroutineRule.testDispatcher,
            )
    }

    @Test
    fun `request reset success`() =
        runTest {
            val email = "test@test.com"
            coEvery {
                requestPasswordResetUseCase.invoke(email)
            } returns MiraiLinkResult.Success("token")

            viewModel.onEmailChanged(email)
            viewModel.requestReset()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value.step == 2)
            assert(viewModel.state.value.error == null)
        }

    @Test
    fun `request reset error`() =
        runTest {
            val email = "test@test.com"
            val errorMessage = "Error message"
            coEvery {
                requestPasswordResetUseCase.invoke(email)
            } returns MiraiLinkResult.Error(errorMessage)

            viewModel.onEmailChanged(email)
            viewModel.requestReset()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value.step == 1)
            assert(viewModel.state.value.error == errorMessage)
        }

    @Test
    fun `confirm reset success`() =
        runTest {
            val email = "test@test.com"
            val token = "token"
            val newPassword = "newPassword"
            coEvery {
                confirmPasswordResetUseCase.invoke(email, token, newPassword)
            } returns MiraiLinkResult.Success("Success")

            viewModel.onEmailChanged(email)
            viewModel.onTokenChanged(token)
            viewModel.onPasswordChanged(newPassword)

            var onConfirmedCalled = false
            viewModel.confirmReset { onConfirmedCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(onConfirmedCalled)
            assert(viewModel.state.value.step == 1) // Resets to initial state
        }

    @Test
    fun `confirm reset error`() =
        runTest {
            val email = "test@test.com"
            val token = "token"
            val newPassword = "newPassword"
            val errorMessage = "Error message"
            coEvery {
                confirmPasswordResetUseCase.invoke(email, token, newPassword)
            } returns MiraiLinkResult.Error(errorMessage)

            viewModel.onEmailChanged(email)
            viewModel.onTokenChanged(token)
            viewModel.onPasswordChanged(newPassword)

            var onConfirmedCalled = false
            viewModel.confirmReset { onConfirmedCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!onConfirmedCalled)
            assert(viewModel.state.value.error == errorMessage)
        }
}
