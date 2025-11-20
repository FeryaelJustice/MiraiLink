// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.auth

import com.feryaeljustice.mirailink.domain.core.JwtUtils
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.LoginVerifyTwoFactorLastStepUseCase
import com.feryaeljustice.mirailink.domain.util.CredentialHelper
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class AuthViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val loginUseCase: LoginUseCase by inject()
    private val registerUseCase: RegisterUseCase by inject()
    private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase by inject()
    private val loginVerifyTwoFactorLastStepUseCase: LoginVerifyTwoFactorLastStepUseCase by inject()
    private val analytics: AnalyticsTracker by inject()
    private val crash: CrashReporter by inject()
    private val credentialHelper: CredentialHelper by inject()

    private lateinit var viewModel: AuthViewModel

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<LoginUseCase>() }
                single { mockk<RegisterUseCase>() }
                single { mockk<GetTwoFactorStatusUseCase>() }
                single { mockk<LoginVerifyTwoFactorLastStepUseCase>() }
                single { mockk<AnalyticsTracker>(relaxed = true) }
                single { mockk<CrashReporter>(relaxed = true) }
                single { mockk<CredentialHelper>(relaxed = true) }
            },
        )
    }

    @Before
    fun setUp() {
        // Mock the JwtUtils object to control the behavior of `extractUserId`
        mockkObject(JwtUtils)

        viewModel = AuthViewModel(
            lazy { loginUseCase },
            lazy { registerUseCase },
            lazy { getTwoFactorStatusUseCase },
            lazy { loginVerifyTwoFactorLastStepUseCase },
            lazy { analytics },
            lazy { crash },
            lazy { credentialHelper },
            mainCoroutineRule.testDispatcher,
            mainCoroutineRule.testDispatcherUnconfined,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login success without 2fa`() =
        runTest {
            val email = "test@test.com"
            val password = "password"
            val token = "a-valid-jwt"
            val userId = "1234567890"

            // Stub the static call to extractUserId
            every { JwtUtils.extractUserId(token) } returns userId
            coEvery { loginUseCase.invoke(email, "", password) } returns
                MiraiLinkResult.Success(
                    token,
                )
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns
                MiraiLinkResult.Success(
                    false,
                )

            var sessionSaved = false
            viewModel.login(email, "", password) { _, _ -> sessionSaved = true }

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value is AuthViewModel.AuthUiState.Success)
            assert(sessionSaved)
        }

    @Test
    fun `login fails`() =
        runTest {
            val email = "test@test.com"
            val password = "password"
            val errorMessage = "Invalid credentials"

            coEvery { loginUseCase.invoke(email, "", password) } returns
                MiraiLinkResult.Error(
                    errorMessage,
                )

            viewModel.login(email, "", password) { _, _ -> }

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assert(state is AuthViewModel.AuthUiState.Error)
            assert((state as AuthViewModel.AuthUiState.Error).message == errorMessage)
        }

    @Test
    fun `register success`() =
        runTest {
            val email = "test@test.com"
            val password = "password"
            val username = "testuser"
            val token = "a-valid-jwt"
            val userId = "1234567890"

            // Stub the static call to extractUserId
            every { JwtUtils.extractUserId(token) } returns userId
            coEvery {
                registerUseCase.invoke(username, email, password)
            } returns MiraiLinkResult.Success(token)
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns
                MiraiLinkResult.Success(
                    false,
                )

            var sessionSaved = false
            viewModel.register(username, email, password) { _, _ -> sessionSaved = true }

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value is AuthViewModel.AuthUiState.Success)
            assert(sessionSaved)
        }
}
