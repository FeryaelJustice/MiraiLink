package com.feryaeljustice.mirailink.ui.screens.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatScreen
import com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatUiState
import com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatViewModel
import com.feryaeljustice.mirailink.ui.screens.onboarding.OnboardingScreen
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreen
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreenViewModel
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import com.feryaeljustice.mirailink.ui.testing.testSession
import com.google.common.truth.Truth.assertThat
import com.feryaeljustice.mirailink.ui.viewentries.VersionCheckResultViewEntry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Isolated Compose tests for onboarding, AI chat and splash entry screens. */
@RunWith(AndroidJUnit4::class)
class AppScreensTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    /** Verifies onboarding completes only after traversing all three pages. */
    @Test
    fun onboardingScreen_completesLastPage() {
        // Given
        var completed = false
        composeRule.setMiraiLinkContent {
            OnboardingScreen(
                onFinish = { completed = true },
                modifier = Modifier.testTag("onboarding-screen"),
            )
        }

        // When
        repeat(2) {
            composeRule.onNodeWithText(composeRule.activity.getString(R.string.next)).performClick()
            composeRule.waitForIdle()
        }
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.start)).performClick()
        composeRule.waitForIdle()

        // Then
        composeRule.onNodeWithTag("onboarding-screen").assertIsDisplayed()
        assertThat(completed).isTrue()
    }

    /** Verifies AI chat accepts a prompt and delegates the send action. */
    @Test
    fun aiChatScreen_sendsEnteredPrompt() {
        // Given
        val viewModel = mockk<AiChatViewModel>(relaxed = true) {
            every { uiState } returns MutableStateFlow<AiChatUiState>(AiChatUiState.Idle)
        }
        val promptLabel = composeRule.activity.getString(R.string.chat_screen_send_msg)
        composeRule.setMiraiLinkContent {
            AiChatScreen(
                miraiLinkSession = testSession(),
                modifier = Modifier.testTag("ai-chat-screen"),
                viewModel = viewModel,
            )
        }

        // When
        composeRule.onNodeWithText(promptLabel).performTextInput("Hello AI")
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.send)).performClick()

        // Then
        composeRule.onNodeWithTag("ai-chat-screen").assertIsDisplayed()
        verify(exactly = 1) { viewModel.sendMessage("Hello AI") }
    }

    /** Verifies splash loading can render without navigating or opening an update gate. */
    @Test
    fun splashScreen_rendersLoadingState() {
        // Given
        val viewModel = mockk<SplashScreenViewModel>(relaxed = true) {
            every { uiState } returns
                MutableStateFlow<SplashScreenViewModel.SplashUiState>(
                    SplashScreenViewModel.SplashUiState.Loading,
                )
            every { updateDiagInfo } returns MutableStateFlow<VersionCheckResultViewEntry?>(null)
        }
        composeRule.setMiraiLinkContent {
            SplashScreen(
                miraiLinkSession = testSession(userId = null),
                onInitialNavigation = {},
                modifier = Modifier.testTag("splash-screen"),
                viewModel = viewModel,
            )
        }

        // Then
        composeRule.onNodeWithTag("splash-screen").assertIsDisplayed()
    }
}
