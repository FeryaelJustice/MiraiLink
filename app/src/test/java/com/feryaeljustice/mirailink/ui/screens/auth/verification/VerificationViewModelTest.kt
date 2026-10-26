package com.feryaeljustice.mirailink.ui.screens.auth.verification

import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.ui.error.toUiError
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
    val koinTestRule =
        KoinTestRule.create {
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
            } returns MiraiLinkResult.Success(Unit)

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
            } returns MiraiLinkResult.Success(Unit)

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
            } returns MiraiLinkResult.Error(UnknownError)

            viewModel.onTokenChanged(token)

            var onFinishCalled = false
            viewModel.confirmCode(userId) { onFinishCalled = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!onFinishCalled)
            assert(viewModel.state.value.error == UnknownError.toUiError())
        }

    @Test
    fun resendErrorKeepsItsOperationWhenBackgroundStatusCheckRuns() = runTest {
        coEvery { requestVerificationCodeUseCase("user", "email") } returns MiraiLinkResult.Error(
            com.feryaeljustice.mirailink.domain.error.DataError.Network.TIMEOUT,
        )
        coEvery { checkIsVerifiedUseCase() } returns MiraiLinkResult.Success(false)
        viewModel.requestCode("user")
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        val error = viewModel.state.value.error!!
        viewModel.checkUserIsVerified {}
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.recoveryFor(error)()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        io.mockk.coVerify(exactly = 2) { requestVerificationCodeUseCase("user", "email") }
        io.mockk.coVerify(exactly = 1) { checkIsVerifiedUseCase() }
    }

    @Test
    fun requestShowsLoadingAndOnlyReportsSentAfterSuccess() = runTest {
        val result = kotlinx.coroutines.CompletableDeferred<MiraiLinkResult<Unit>>()
        coEvery { requestVerificationCodeUseCase("user", "email") } coAnswers { result.await() }
        var sent = 0
        viewModel.requestCode("user") { sent++ }
        mainCoroutineRule.testDispatcher.scheduler.runCurrent()
        assert(viewModel.state.value.isLoading)
        viewModel.requestCode("user") { sent++ }
        mainCoroutineRule.testDispatcher.scheduler.runCurrent()
        io.mockk.coVerify(exactly = 1) { requestVerificationCodeUseCase("user", "email") }
        assert(sent == 0)
        result.complete(MiraiLinkResult.Error(UnknownError))
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        assert(!viewModel.state.value.isLoading)
        assert(viewModel.state.value.error != null)
        assert(sent == 0)
        coEvery { requestVerificationCodeUseCase("user", "email") } returns MiraiLinkResult.Success(Unit)
        viewModel.recoveryFor(viewModel.state.value.error!!)()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        assert(sent == 1)
        assert(!viewModel.state.value.isLoading)
    }
}
