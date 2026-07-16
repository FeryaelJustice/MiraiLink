package com.feryaeljustice.mirailink.ui.screens.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordScreen
import com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordViewModel
import com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationScreen
import com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationViewModel
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import com.feryaeljustice.mirailink.ui.testing.testSession
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Isolated Compose interaction tests for authentication-related screens. */
@RunWith(AndroidJUnit4::class)
class AuthScreensTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    /** Verifies the authentication screen renders and switches to registration mode. */
    @Test
    fun authScreen_switchesToRegistration() {
        // Given
        val viewModel = authViewModel()
        composeRule.setMiraiLinkContent {
            AuthScreen(
                miraiLinkSession = testSession(userId = null),
                onLogin = {},
                onRegister = {},
                onRequestPasswordReset = {},
                modifier = Modifier.testTag("auth-screen"),
                viewModel = viewModel,
            )
        }

        // When
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.auth_screen_register))
            .performClick()

        // Then
        composeRule.onNodeWithTag("auth-screen").assertIsDisplayed()
        verify(atLeast = 1) { viewModel.resetScreenVMState() }
    }

    /** Verifies the first password-recovery step delegates its primary action. */
    @Test
    fun recoverPasswordScreen_requestsCode() {
        // Given
        val viewModel = mockk<RecoverPasswordViewModel>(relaxed = true) {
            every { state } returns
                MutableStateFlow(
                    RecoverPasswordViewModel.PasswordResetState(email = "person@example.com"),
                )
        }
        composeRule.setMiraiLinkContent {
            RecoverPasswordScreen(
                miraiLinkSession = testSession(userId = null),
                email = "person@example.com",
                onConfirmedRecoverPassword = {},
                modifier = Modifier.testTag("recover-password-screen"),
                viewModel = viewModel,
            )
        }

        // When
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.send_code)).performClick()

        // Then
        composeRule.onNodeWithTag("recover-password-screen").assertIsDisplayed()
        verify(exactly = 1) { viewModel.requestReset() }
    }

    /** Verifies verification renders its request step and delegates the selected user id. */
    @Test
    fun verificationScreen_requestsCodeForUser() {
        // Given
        val viewModel = mockk<VerificationViewModel>(relaxed = true) {
            every { state } returns MutableStateFlow(VerificationViewModel.VerificationState())
        }
        composeRule.setMiraiLinkContent {
            VerificationScreen(
                miraiLinkSession = testSession(userId = "42"),
                userId = "42",
                onFinish = {},
                modifier = Modifier.testTag("verification-screen"),
                viewModel = viewModel,
            )
        }

        // When
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.send_code)).performClick()

        // Then
        composeRule.onNodeWithTag("verification-screen").assertIsDisplayed()
        verify(exactly = 1) { viewModel.requestCode("42") }
    }

    /** Creates the many-flow authentication double used by the screen contract. */
    private fun authViewModel() =
        mockk<AuthViewModel>(relaxed = true) {
            every { state } returns MutableStateFlow<AuthViewModel.AuthUiState>(AuthViewModel.AuthUiState.Idle)
            every { loginByUsername } returns MutableStateFlow(true)
            every { usernameError } returns MutableStateFlow<AuthViewModel.AuthFieldError?>(null)
            every { emailError } returns MutableStateFlow<AuthViewModel.AuthFieldError?>(null)
            every { passwordError } returns MutableStateFlow<AuthViewModel.AuthFieldError?>(null)
            every { confirmPasswordError } returns MutableStateFlow<AuthViewModel.AuthFieldError?>(null)
            every { userId } returns MutableStateFlow<String?>(null)
            every { showTwoFactorLastStepDialog } returns MutableStateFlow(false)
            every { twoFactorLastStepDialogIsLoading } returns MutableStateFlow(false)
            every { twoFactorCode } returns MutableStateFlow("")
        }
}
