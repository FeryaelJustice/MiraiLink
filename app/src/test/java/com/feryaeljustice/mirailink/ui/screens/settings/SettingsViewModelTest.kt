package com.feryaeljustice.mirailink.ui.screens.settings

import app.cash.turbine.test
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SettingsViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val logoutUseCase: LogoutUseCase by inject()
    private val deleteAccountUseCase: DeleteAccountUseCase by inject()

    private lateinit var viewModel: SettingsViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<LogoutUseCase>() }
                    single { mockk<DeleteAccountUseCase>() }
                },
            )
        }

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
            coEvery { logoutUseCase.invoke() } returns MiraiLinkResult.Success(Unit)

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
            coEvery { logoutUseCase.invoke() } returns MiraiLinkResult.Error("Error")

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
            coEvery { deleteAccountUseCase.invoke() } returns MiraiLinkResult.Success(Unit)

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
            coEvery { deleteAccountUseCase.invoke() } returns MiraiLinkResult.Error("Error")

            var onFinishCalled = false
            viewModel.deleteAccount { onFinishCalled = true }

            viewModel.deleteSuccess.test {
                assertEquals(false, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assert(!onFinishCalled)
        }
}
