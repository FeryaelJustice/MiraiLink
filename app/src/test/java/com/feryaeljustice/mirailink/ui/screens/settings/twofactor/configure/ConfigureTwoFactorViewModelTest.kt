package com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure

import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.DisableTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase
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
class ConfigureTwoFactorViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val setupTwoFactorUseCase: SetupTwoFactorUseCase by inject()
    private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase by inject()
    private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase by inject()
    private val disableTwoFactorUseCase: DisableTwoFactorUseCase by inject()

    private val requestEmail = mockk<com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.RequestTwoFactorEmailUseCase>()

    private lateinit var viewModel: ConfigureTwoFactorViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<SetupTwoFactorUseCase>() }
                    single { mockk<VerifyTwoFactorUseCase>() }
                    single { mockk<GetTwoFactorStatusUseCase>() }
                    single { mockk<DisableTwoFactorUseCase>() }
                },
            )
        }

    @Before
    fun setUp() {
        coEvery { requestEmail.invoke(any()) } returns MiraiLinkResult.Success(Unit)
        viewModel =
            ConfigureTwoFactorViewModel(
                setupTwoFactorUseCase,
                verifyTwoFactorUseCase,
                getTwoFactorStatusUseCase,
                disableTwoFactorUseCase,
                mainCoroutineRule.testDispatcher,
                requestEmail,
            )
    }

    @Test
    fun `launch setup two factor dialog success`() =
        runTest {
            val setupInfo = TwoFactorAuthInfo(true, "otpUrl", "base32", listOf("recovery"))
            coEvery { setupTwoFactorUseCase.invoke() } returns MiraiLinkResult.Success(setupInfo)

            viewModel.launchSetupTwoFactorDialog()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.showSetupDialog.value)
            assert(viewModel.otpUrl.value == "otpUrl")
        }

    @Test
    fun `confirm setup two factor success`() =
        runTest {
            val userId = "userId"
            val code = "123456"
            coEvery { verifyTwoFactorUseCase.invoke(code, "654321") } returns MiraiLinkResult.Success(Unit)
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns
                MiraiLinkResult.Success(
                    true,
                )

            viewModel.requestEmailCode("enable")
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            viewModel.onEmailCodeChanged("654321")
            viewModel.onSetupTwoFactorCodeChanged(code)
            viewModel.confirmSetupTwoFactor(userId)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!viewModel.showSetupDialog.value)
            assert(viewModel.isTwoFactorEnabled.value)
        }

    @Test
    fun `disable two factor success`() =
        runTest {
            val userId = "userId"
            val code = "123456"
            coEvery { disableTwoFactorUseCase.invoke(any()) } returns MiraiLinkResult.Success(Unit)
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns
                MiraiLinkResult.Success(
                    false,
                )

            viewModel.requestEmailCode("disable")
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            viewModel.onEmailCodeChanged(code)
            viewModel.confirmDisableTwoFactor(userId)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!viewModel.showDisableTwoFactorDialog.value)
            assert(!viewModel.isTwoFactorEnabled.value)
        }

    @Test
    fun rejectsActivationWithoutDeliveredEmailCode() = runTest {
        viewModel.onSetupTwoFactorCodeChanged("123456")
        viewModel.onEmailCodeChanged("654321")
        viewModel.confirmSetupTwoFactor("user")
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        io.mockk.coVerify(exactly = 0) { verifyTwoFactorUseCase.invoke(any(), any()) }
    }

    @Test
    fun failedEmailDeliveryLeavesConfirmationDisabled() = runTest {
        coEvery { requestEmail.invoke("disable") } returns MiraiLinkResult.Error(
            com.feryaeljustice.mirailink.domain.error.DataError.Network.SERVICE_UNAVAILABLE,
        )
        viewModel.requestEmailCode("disable")
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        org.junit.Assert.assertFalse(viewModel.emailCodeSent.value)
        org.junit.Assert.assertFalse(viewModel.isSendingEmailCode.value)
        org.junit.Assert.assertNotNull(viewModel.errorString.value)
        viewModel.confirmDisableTwoFactor("user")
        io.mockk.coVerify(exactly = 0) { disableTwoFactorUseCase.invoke(any()) }
    }

    @Test
    fun resendAndCancelDiscardOldCode() = runTest {
        viewModel.requestEmailCode("enable")
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEmailCodeChanged("654321")
        viewModel.requestEmailCode("enable")
        org.junit.Assert.assertEquals("", viewModel.emailCode.value)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEmailCodeChanged("654321")
        viewModel.dismissSetupTwoFactorDialog()
        org.junit.Assert.assertEquals("", viewModel.emailCode.value)
        org.junit.Assert.assertFalse(viewModel.emailCodeSent.value)
    }
}
