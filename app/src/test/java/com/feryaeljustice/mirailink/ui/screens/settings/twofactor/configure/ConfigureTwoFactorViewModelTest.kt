// Author: Feryael Justice
// Date: 2025-11-08

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

@ExperimentalCoroutinesApi
class ConfigureTwoFactorViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ConfigureTwoFactorViewModel
    private val setupTwoFactorUseCase: SetupTwoFactorUseCase = mockk()
    private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase = mockk()
    private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase = mockk()
    private val disableTwoFactorUseCase: DisableTwoFactorUseCase = mockk()

    @Before
    fun setUp() {
        viewModel =
            ConfigureTwoFactorViewModel(
                setupTwoFactorUseCase,
                verifyTwoFactorUseCase,
                getTwoFactorStatusUseCase,
                disableTwoFactorUseCase,
                mainCoroutineRule.testDispatcher,
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
            coEvery { verifyTwoFactorUseCase.invoke(code) } returns MiraiLinkResult.Success(Unit)
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns
                MiraiLinkResult.Success(
                    true,
                )

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
            coEvery { disableTwoFactorUseCase.invoke(code) } returns MiraiLinkResult.Success(Unit)
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns
                MiraiLinkResult.Success(
                    false,
                )

            viewModel.onDisableTwoFactorCodeChanged(code)
            viewModel.confirmDisableTwoFactor(userId)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!viewModel.showDisableTwoFactorDialog.value)
            assert(!viewModel.isTwoFactorEnabled.value)
        }
}
