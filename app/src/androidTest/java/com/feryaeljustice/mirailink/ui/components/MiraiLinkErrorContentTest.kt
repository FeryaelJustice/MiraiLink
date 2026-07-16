package com.feryaeljustice.mirailink.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.error.ErrorRecovery
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.UiText
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Instrumented contract tests for localized actionable error rendering. */
@RunWith(AndroidJUnit4::class)
class MiraiLinkErrorContentTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    /** Verifies resource resolution and exact recovery callback delegation. */
    @Test
    fun errorContent_rendersMessageAndInvokesAction() {
        // Given
        var actionCalls = 0
        val error =
            UiError(
                message = UiText.Resource(R.string.error_no_connection),
                actionLabel = UiText.Resource(R.string.action_retry),
                recovery = ErrorRecovery.RETRY,
            )
        composeRule.setMiraiLinkContent {
            MiraiLinkErrorContent(error = error, onAction = { actionCalls++ })
        }

        // Then
        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.error_no_connection),
        ).assertIsDisplayed()

        // When
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.action_retry))
            .performClick()

        // Then
        assertThat(actionCalls).isEqualTo(1)
    }
}
