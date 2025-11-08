// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.splash

import com.feryaeljustice.mirailink.domain.model.VersionCheckResult
import com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SplashScreenViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val checkAppVersionUseCase: CheckAppVersionUseCase = mockk()
    private val autologinUseCase: Lazy<AutologinUseCase> = mockk()
    private val checkOnboardingIsCompletedUseCase: Lazy<CheckOnboardingIsCompleted> = mockk()

    private lateinit var viewModel: SplashScreenViewModel

    @Test
    fun `when onboarding is not completed, navigate to onboarding`() =
        runTest {
            coEvery { checkAppVersionUseCase.invoke(any()) } returns
                MiraiLinkResult.Success(
                    VersionCheckResult(
                        mustUpdate = false,
                        shouldUpdate = false,
                        "",
                        "",
                    ),
                )
            coEvery {
                checkOnboardingIsCompletedUseCase.get().invoke()
            } returns MiraiLinkResult.Success(false)
            coEvery { autologinUseCase.get().invoke() } returns MiraiLinkResult.Error("")

            viewModel =
                SplashScreenViewModel(
                    checkAppVersionUseCase,
                    autologinUseCase,
                    checkOnboardingIsCompletedUseCase,
                    mainCoroutineRule.testDispatcher,
                    mainCoroutineRule.testDispatcher,
                )
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assert(state is SplashScreenViewModel.SplashUiState.Navigate)
            assert((state as SplashScreenViewModel.SplashUiState.Navigate).action == InitialNavigationAction.GoToOnboarding)
        }

    @Test
    fun `when onboarding is completed and autologin succeeds, navigate to home`() =
        runTest {
            coEvery { checkAppVersionUseCase.invoke(any()) } returns
                MiraiLinkResult.Success(
                    VersionCheckResult(
                        mustUpdate = false,
                        shouldUpdate = false,
                        "",
                        "",
                    ),
                )
            coEvery {
                checkOnboardingIsCompletedUseCase.get().invoke()
            } returns MiraiLinkResult.Success(true)
            coEvery { autologinUseCase.get().invoke() } returns MiraiLinkResult.Success("token")

            viewModel =
                SplashScreenViewModel(
                    checkAppVersionUseCase,
                    autologinUseCase,
                    checkOnboardingIsCompletedUseCase,
                    mainCoroutineRule.testDispatcher,
                    mainCoroutineRule.testDispatcher,
                )
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assert(state is SplashScreenViewModel.SplashUiState.Navigate)
            assert((state as SplashScreenViewModel.SplashUiState.Navigate).action == InitialNavigationAction.GoToHome)
        }

    @Test
    fun `when onboarding is completed and autologin fails, navigate to auth`() =
        runTest {
            coEvery { checkAppVersionUseCase.invoke(any()) } returns
                MiraiLinkResult.Success(
                    VersionCheckResult(
                        mustUpdate = false,
                        shouldUpdate = false,
                        "",
                        "",
                    ),
                )
            coEvery {
                checkOnboardingIsCompletedUseCase.get().invoke()
            } returns MiraiLinkResult.Success(true)
            coEvery { autologinUseCase.get().invoke() } returns MiraiLinkResult.Error("")

            viewModel =
                SplashScreenViewModel(
                    checkAppVersionUseCase,
                    autologinUseCase,
                    checkOnboardingIsCompletedUseCase,
                    mainCoroutineRule.testDispatcher,
                    mainCoroutineRule.testDispatcher,
                )
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assert(state is SplashScreenViewModel.SplashUiState.Navigate)
            assert((state as SplashScreenViewModel.SplashUiState.Navigate).action == InitialNavigationAction.GoToAuth)
        }

    @Test
    fun `when must update, show update dialog`() =
        runTest {
            coEvery { checkAppVersionUseCase.invoke(any()) } returns
                MiraiLinkResult.Success(
                    VersionCheckResult(
                        mustUpdate = true,
                        shouldUpdate = false,
                        "",
                        "",
                    ),
                )
            coEvery {
                checkOnboardingIsCompletedUseCase.get().invoke()
            } returns MiraiLinkResult.Success(true)
            coEvery { autologinUseCase.get().invoke() } returns MiraiLinkResult.Success("token")

            viewModel =
                SplashScreenViewModel(
                    checkAppVersionUseCase,
                    autologinUseCase,
                    checkOnboardingIsCompletedUseCase,
                    mainCoroutineRule.testDispatcher,
                    mainCoroutineRule.testDispatcher,
                )
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.updateDiagInfo.value?.mustUpdate == true)
        }
}
