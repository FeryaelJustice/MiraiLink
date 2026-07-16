package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackScreen
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackState
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorScreen
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorViewModel
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import com.feryaeljustice.mirailink.ui.testing.testSession
import io.mockk.every
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Isolated Compose tests for settings, feedback and two-factor screens. */
@RunWith(AndroidJUnit4::class)
class SettingsScreensTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    /** Verifies settings delegates navigation to feedback. */
    @Test
    fun settingsScreen_opensFeedback() {
        var opened = false
        val viewModel = mockk<SettingsViewModel>(relaxed = true) {
            every { error } returns MutableStateFlow<UiError?>(null)
            every { logoutSuccess } returns MutableSharedFlow<Boolean>()
            every { deleteSuccess } returns MutableSharedFlow<Boolean>()
        }
        composeRule.setMiraiLinkContent {
            SettingsScreen(
                miraiLinkSession = testSession(),
                goToFeedbackScreen = { opened = true },
                goToConfigureTwoFactorScreen = {},
                showToast = { _, _ -> },
                copyToClipBoard = {},
                modifier = Modifier.testTag("settings-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.settings_screen_txt_give_feedback),
        ).performClick()

        composeRule.onNodeWithTag("settings-screen").assertIsDisplayed()
        assertThat(opened).isTrue()
    }

    /** Verifies feedback's empty form renders without triggering submission. */
    @Test
    fun feedbackScreen_rendersEmptyForm() {
        val viewModel = mockk<FeedbackViewModel>(relaxed = true) {
            every { uiState } returns MutableStateFlow(FeedbackState())
        }
        composeRule.setMiraiLinkContent {
            FeedbackScreen(
                showToast = { _, _ -> },
                onBackClick = {},
                modifier = Modifier.testTag("feedback-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithTag("feedback-screen").assertIsDisplayed()
        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.feedback_screen_send_feedback),
        ).assertIsDisplayed()
    }

    /** Verifies two-factor configuration exposes back navigation and status content. */
    @Test
    fun configureTwoFactorScreen_navigatesBack() {
        var wentBack = false
        val viewModel = twoFactorViewModel()
        composeRule.setMiraiLinkContent {
            ConfigureTwoFactorScreen(
                miraiLinkSession = testSession(),
                onBackClick = { wentBack = true },
                modifier = Modifier.testTag("two-factor-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.back))
            .performClick()

        composeRule.onNodeWithTag("two-factor-screen").assertIsDisplayed()
        assertThat(wentBack).isTrue()
    }

    /** Creates the full two-factor observable contract with dialog-free defaults. */
    private fun twoFactorViewModel() =
        mockk<ConfigureTwoFactorViewModel>(relaxed = true) {
            every { isTwoFactorEnabled } returns MutableStateFlow(false)
            every { showSetupDialog } returns MutableStateFlow(false)
            every { isConfigure2FALoading } returns MutableStateFlow(false)
            every { showDisableTwoFactorDialog } returns MutableStateFlow(false)
            every { isDisable2FALoading } returns MutableStateFlow(false)
            every { otpUrl } returns MutableStateFlow<String?>(null)
            every { base32 } returns MutableStateFlow("")
            every { recoveryCodes } returns MutableStateFlow<List<String>>(emptyList())
            every { verify2FACode } returns MutableStateFlow("")
            every { disable2FACode } returns MutableStateFlow("")
            every { errorString } returns MutableStateFlow<UiError?>(null)
        }
}
