package com.feryaeljustice.mirailink.ui.screens.auth

import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.domain.core.JwtUtils
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase
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
    private val checkIsVerifiedUseCase: CheckIsVerifiedUseCase by inject()
    private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase by inject()
    private val loginVerifyTwoFactorLastStepUseCase: LoginVerifyTwoFactorLastStepUseCase by inject()
    private val analytics: AnalyticsTracker by inject()
    private val crash: CrashReporter by inject()
    private val credentialHelper: CredentialHelper by inject()
    private val sessionManager: SessionManager by inject()

    private lateinit var viewModel: AuthViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<LoginUseCase>() }
                    single { mockk<RegisterUseCase>() }
                    single { mockk<CheckIsVerifiedUseCase>() }
                    single { mockk<GetTwoFactorStatusUseCase>() }
                    single { mockk<LoginVerifyTwoFactorLastStepUseCase>() }
                    single { mockk<AnalyticsTracker>(relaxed = true) }
                    single { mockk<CrashReporter>(relaxed = true) }
                    single { mockk<CredentialHelper>(relaxed = true) }
                    single { mockk<SessionManager>(relaxed = true) }
                },
            )
        }

    @Before
    fun setUp() {
        // Mock the JwtUtils object to control the behavior of `extractUserId`
        mockkObject(JwtUtils)

        viewModel =
            AuthViewModel(
                lazy { loginUseCase },
                lazy { registerUseCase },
                lazy { checkIsVerifiedUseCase },
                lazy { getTwoFactorStatusUseCase },
                lazy { loginVerifyTwoFactorLastStepUseCase },
                lazy { analytics },
                lazy { crash },
                lazy { credentialHelper },
                sessionManager,
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
            coEvery { checkIsVerifiedUseCase.invoke() } returns MiraiLinkResult.Success(true)

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
                MiraiLinkResult.Error(UnknownError)

            viewModel.login(email, "", password) { _, _ -> }

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assert(state is AuthViewModel.AuthUiState.Error)
            assert((state as AuthViewModel.AuthUiState.Error).error == UnknownError.toUiError())
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
            coEvery { checkIsVerifiedUseCase.invoke() } returns MiraiLinkResult.Success(true)

            var sessionSaved = false
            viewModel.register(username, email, password) { _, _ -> sessionSaved = true }

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value is AuthViewModel.AuthUiState.Success)
            assert(sessionSaved)
        }

    @Test
    fun `login with an unverified account waits for verification before saving the session`() =
        runTest {
            val email = "test@test.com"
            val password = "password"
            val token = "a-valid-jwt"
            val userId = "1234567890"

            every { JwtUtils.extractUserId(token) } returns userId
            coEvery { loginUseCase.invoke(email, "", password) } returns MiraiLinkResult.Success(token)
            coEvery { getTwoFactorStatusUseCase.invoke(userId) } returns MiraiLinkResult.Success(false)
            coEvery { checkIsVerifiedUseCase.invoke() } returns MiraiLinkResult.Success(false)

            var sessionSaved = false
            viewModel.login(email, "", password) { _, _ -> sessionSaved = true }
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.state.value is AuthViewModel.AuthUiState.VerificationRequired)
            assert(!sessionSaved)
        }

    @Test
    fun `validateFields rejects passwords shorter than 8 characters`() =
        runTest {
            val valid = viewModel.validateFields(
                isLogin = false,
                username = "testuser",
                email = "test@example.com",
                password = "short",
                confirmPassword = "short",
            )

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!valid)
            assert(viewModel.passwordError.value is AuthViewModel.AuthFieldError.MinLength)
            val minError = viewModel.passwordError.value as AuthViewModel.AuthFieldError.MinLength
            assert(minError.min == 8)
        }

    @Test
    fun `validateFields rejects trivial sequential passwords`() =
        runTest {
            val valid = viewModel.validateFields(
                isLogin = false,
                username = "testuser",
                email = "test@example.com",
                password = "12345678",
                confirmPassword = "12345678",
            )

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!valid)
            assert(viewModel.passwordError.value is AuthViewModel.AuthFieldError.TrivialPassword)
        }

    @Test
    fun `validateFields accepts non-trivial passwords of at least 8 characters`() =
        runTest {
            val valid = viewModel.validateFields(
                isLogin = false,
                username = "testuser",
                email = "test@example.com",
                password = "SecurePassword2026!",
                confirmPassword = "SecurePassword2026!",
            )

            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(valid)
            assert(viewModel.passwordError.value == null)
        }
}
