// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.auth.verification

import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase
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
class VerificationViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val checkIsVerifiedUseCase: CheckIsVerifiedUseCase by inject()
    private val requestVerificationCodeUseCase: RequestVerificationCodeUseCase by inject()
    private val confirmVerificationCodeUseCase: ConfirmVerificationCodeUseCase by inject()

    private lateinit var viewModel: VerificationViewModel

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<CheckIsVerifiedUseCase>() }
                single { mockk<RequestVerificationCodeUseCase>() }
                single { mockk<ConfirmVerificationCodeUseCase>() }
            },
        )
    }

    @Before
    fun setUp() {
        viewModel =
            VerificationViewModel(
                checkIsVerifiedUseCase,
                requestVerificationCodeUseCase,
                confirmVerificationCodeUseCase,
                mainCoroutineRule.testDispatcher,
            )
    }

    @Test
    fun `user is already verified`() =
        runTest {
            coEvery { checkIsVerifiedUseCase.invoke() } returns MiraiLinkResult.Success(true)

            var isVerified = false
            viewModel.checkUserIsVerified { isVerified = it }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(isVerified)
            assert(viewModel.state.value.step == 1) // Resets
        }

    @Test
    fun `request code success`() =
        runTest {
            val userId = "userId"
            coEvery {
                requestVerificationCodeUseCase.invoke(userId, "email")
            } returns MiraiLinkResult.Success("Success")

            viewModel.requestCode(userId)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value.step == 2)
            assert(viewModel.state.value.error == null)
        }

    @Test
    fun `confirm code success`() =
        runTest {
            val userId = "userId"
            val token = "token"
            coEvery {
                confirmVerificationCodeUseCase.invoke(userId, token, "email")
            } returns MiraiLinkResult.Success("Success")

            viewModel.onTokenChanged(token)

            var onFinishCalled = false
            viewModel.confirmCode(userId) { onFinishCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(onFinishCalled)
            assert(viewModel.state.value.step == 1) // Resets
        }

    @Test
    fun `confirm code error`() =
        runTest {
            val userId = "userId"
            val token = "token"
            val errorMessage = "Error message"
            coEvery {
                confirmVerificationCodeUseCase.invoke(userId, token, "email")
            } returns MiraiLinkResult.Error(errorMessage)

            viewModel.onTokenChanged(token)

            var onFinishCalled = false
            viewModel.confirmCode(userId) { onFinishCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!onFinishCalled)
            assert(viewModel.state.value.error == errorMessage)
        }
}
