package com.feryaeljustice.mirailink.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorPutCodeOrRecoveryCDialog
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MiraiLinkDialogTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initiallyLoadingTwoFactorDialogRestoresConfirmationAfterFailure() {
        val loading = mutableStateOf(true)
        var confirmations = 0
        composeRule.setMiraiLinkContent {
            TwoFactorPutCodeOrRecoveryCDialog(
                code = "123456",
                isLoading = loading.value,
                onCodeChange = {},
                onDismiss = {},
                onConfirm = { confirmations++ },
                isDisable = false,
            )
        }
        val accept = composeRule.activity.getString(R.string.accept)
        composeRule.onNodeWithText(accept).assertDoesNotExist()
        composeRule.runOnIdle { loading.value = false }
        composeRule.onNodeWithText(accept).assertIsDisplayed().performClick()
        composeRule.runOnIdle { assertEquals(1, confirmations) }
        composeRule.runOnIdle { loading.value = true }
        composeRule.waitForIdle()
        composeRule.onNodeWithText(accept).assertDoesNotExist()
    }
}
