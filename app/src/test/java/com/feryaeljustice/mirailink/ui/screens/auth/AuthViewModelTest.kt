/**
 * @author Feryael Justice
 * @since 24/07/2024
 */
package com.feryaeljustice.mirailink.ui.screens.auth

import com.feryaeljustice.mirailink.domain.core.JwtUtils
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.LoginVerifyTwoFactorLastStepUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AuthViewModel
    private val loginUseCase: Lazy<LoginUseCase> = mockk()
    private val registerUseCase: Lazy<RegisterUseCase> = mockk()
    private val getTwoFactorStatusUseCase: Lazy<GetTwoFactorStatusUseCase> = mockk()
    private val loginVerifyTwoFactorLastStepUseCase: Lazy<LoginVerifyTwoFactorLastStepUseCase> = mockk()
    private val analytics: AnalyticsTracker = mockk(relaxed = true)
    private val crash: CrashReporter = mockk(relaxed = true)

    @Before
    fun setUp() {
        // Mock the JwtUtils object to control the behavior of `extractUserId`
        mockkObject(JwtUtils)

        viewModel =
            AuthViewModel(
                loginUseCase,
                registerUseCase,
                getTwoFactorStatusUseCase,
                loginVerifyTwoFactorLastStepUseCase,
                analytics,
                crash,
                mainCoroutineRule.testDispatcher,
            )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login success without 2fa`() = runTest {
        val email = "test@test.com"
        val password = "password"
        val token = "a-valid-jwt"
        val userId = "1234567890"

        // Stub the static call to extractUserId
        every { JwtUtils.extractUserId(token) } returns userId
        coEvery { loginUseCase.get().invoke(email, "", password) } returns MiraiLinkResult.Success(token)
        coEvery { getTwoFactorStatusUseCase.get().invoke(userId) } returns MiraiLinkResult.Success(false)

        var sessionSaved = false
        viewModel.login(email, "", password) { _, _ -> sessionSaved = true }

        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state.value is AuthViewModel.AuthUiState.Success)
        assert(sessionSaved)
        verify { analytics.logEvent("login_success") }
    }

    @Test
    fun `login fails`() = runTest {
        val email = "test@test.com"
        val password = "password"
        val errorMessage = "Invalid credentials"

        coEvery { loginUseCase.get().invoke(email, "", password) } returns MiraiLinkResult.Error(errorMessage)

        viewModel.login(email, "", password) { _, _ -> }

        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is AuthViewModel.AuthUiState.Error)
        assert((state as AuthViewModel.AuthUiState.Error).message == errorMessage)
        verify { crash.recordNonFatal(any()) }
    }

    @Test
    fun `register success`() = runTest {
        val email = "test@test.com"
        val password = "password"
        val username = "testuser"
        val token = "a-valid-jwt"
        val userId = "1234567890"

        // Stub the static call to extractUserId
        every { JwtUtils.extractUserId(token) } returns userId
        coEvery { registerUseCase.get().invoke(username, email, password) } returns MiraiLinkResult.Success(token)
        coEvery { getTwoFactorStatusUseCase.get().invoke(userId) } returns MiraiLinkResult.Success(false)

        var sessionSaved = false
        viewModel.register(username, email, password) { _, _ -> sessionSaved = true }

        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state.value is AuthViewModel.AuthUiState.Success)
        assert(sessionSaved)
    }
}
