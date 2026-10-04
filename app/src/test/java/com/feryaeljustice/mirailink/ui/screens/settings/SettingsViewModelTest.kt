package com.feryaeljustice.mirailink.ui.screens.settings

import com.feryaeljustice.mirailink.domain.error.UnknownError
import app.cash.turbine.test
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
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
    private val getCurrentUserUseCase: com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase by inject()
    private val getThemePreferenceUseCase: com.feryaeljustice.mirailink.domain.usecase.settings.GetThemePreferenceUseCase by inject()
    private val setThemePreferenceUseCase: com.feryaeljustice.mirailink.domain.usecase.settings.SetThemePreferenceUseCase by inject()
    private lateinit var viewModel: SettingsViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<LogoutUseCase>() }
                    single { mockk<DeleteAccountUseCase>() }
                    single { mockk<com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase> {
                        coEvery { this@mockk.invoke() } returns MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.UnknownError)
                    } }
                    single { mockk<com.feryaeljustice.mirailink.domain.usecase.settings.GetThemePreferenceUseCase> {
                        every { this@mockk.invoke() } returns kotlinx.coroutines.flow.flowOf(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.SYSTEM)
                    } }
                    single { mockk<com.feryaeljustice.mirailink.domain.usecase.settings.SetThemePreferenceUseCase>(relaxed = true) }
                },
            )
        }

    @Before
    fun setUp() {
        viewModel =
            SettingsViewModel(
                logoutUseCase,
                deleteAccountUseCase,
                getCurrentUserUseCase,
                getThemePreferenceUseCase,
                setThemePreferenceUseCase,
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
            coEvery { logoutUseCase.invoke() } returns MiraiLinkResult.Error(UnknownError)

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
            coEvery { deleteAccountUseCase.invoke() } returns MiraiLinkResult.Error(UnknownError)

            var onFinishCalled = false
            viewModel.deleteAccount { onFinishCalled = true }

            viewModel.deleteSuccess.test {
                assertEquals(false, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assert(!onFinishCalled)
        }

    @Test
    fun `themePreference exposes value from use case`() =
        runTest {
            viewModel.themePreference.test {
                assertEquals(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.SYSTEM, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `setThemePreference calls usecase`() =
        runTest {
            coEvery {
                setThemePreferenceUseCase.invoke(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.DARK)
            } returns MiraiLinkResult.Success(Unit)

            viewModel.setThemePreference(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.DARK)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            io.mockk.coVerify {
                setThemePreferenceUseCase.invoke(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.DARK)
            }
        }

    @Test
    fun `setThemePreference failure sets error`() =
        runTest {
            coEvery {
                setThemePreferenceUseCase.invoke(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.DARK)
            } returns MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.DataError.Local.UNKNOWN)

            viewModel.setThemePreference(com.feryaeljustice.mirailink.domain.model.settings.ThemePreference.DARK)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.error.test {
                val item = awaitItem()
                org.junit.Assert.assertNotNull(item)
                cancelAndConsumeRemainingEvents()
            }
        }
}
