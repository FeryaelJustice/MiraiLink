// Author: Feryael Justice
// Date: 2024-07-29

package com.feryaeljustice.mirailink.ui.screens.settings

import app.cash.turbine.test
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SettingsViewModel
    private val logoutUseCase: Lazy<LogoutUseCase> = mockk()
    private val deleteAccountUseCase: Lazy<DeleteAccountUseCase> = mockk()

    @Before
    fun setUp() {
        viewModel =
            SettingsViewModel(
                logoutUseCase,
                deleteAccountUseCase,
                mainCoroutineRule.testDispatcher,
                mainCoroutineRule.testDispatcher,
            )
    }

    @Test
    fun `logout success`() =
        runTest {
            coEvery { logoutUseCase.get().invoke() } returns MiraiLinkResult.Success(Unit)

            var onFinishCalled = false
            viewModel.logout { onFinishCalled = true }

            viewModel.logoutSuccess.test {
                assertEquals(true, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assert(onFinishCalled)
        }

    @Test
    fun `logout failure`() =
        runTest {
            coEvery { logoutUseCase.get().invoke() } returns MiraiLinkResult.Error("Error")

            var onFinishCalled = false
            viewModel.logout { onFinishCalled = true }

            viewModel.logoutSuccess.test {
                assertEquals(false, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assert(!onFinishCalled)
        }

    @Test
    fun `delete account success`() =
        runTest {
            coEvery { deleteAccountUseCase.get().invoke() } returns MiraiLinkResult.Success(Unit)

            var onFinishCalled = false
            viewModel.deleteAccount { onFinishCalled = true }

            viewModel.deleteSuccess.test {
                assertEquals(true, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assert(onFinishCalled)
        }

    @Test
    fun `delete account failure`() =
        runTest {
            coEvery { deleteAccountUseCase.get().invoke() } returns MiraiLinkResult.Error("Error")

            var onFinishCalled = false
            viewModel.deleteAccount { onFinishCalled = true }

            viewModel.deleteSuccess.test {
                assertEquals(false, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assert(!onFinishCalled)
        }
}
