// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
class AppE2ETest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appNavigation_login() {
        // 1. Aceptar consentimiento y completar onboarding
        clickConsentBanner("Consent")
        composeTestRule.completeOnboarding()

        // 2. Realizar login de forma segura
        composeTestRule.login(
            username = BuildConfig.TEST_USER,
            password = BuildConfig.TEST_PASS,
        )

        // 3. Verificar que estamos en la pantalla principal esperando a que aparezca un elemento
        composeTestRule.waitUntilNodeWithTag("HomeRefreshBox").assertIsDisplayed()
    }

    @Test
    fun appNavigation_swipeAndUndo() {
        val isHomeVisible =
            composeTestRule
                .onAllNodesWithTag("HomeRefreshBox", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()

        if (!isHomeVisible) {
            appNavigation_login()
        }

        // 1. Swipe right on the first user
        composeTestRule.onNodeWithTag("swipeHome").performTouchInput { swipeRight() }

        // 2. Verify that the user is displayed again
        composeTestRule.onNodeWithTag("swipeHome").assertIsDisplayed()
    }

    @Test
    fun appInteraction_likeAndDismiss() {
        val isHomeVisible =
            composeTestRule
                .onAllNodesWithTag("HomeRefreshBox", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()

        if (!isHomeVisible) {
            appNavigation_login()
        }

        // 1. Click on the like button
        composeTestRule
            .onAllNodesWithTag("likeBtn", useUnmergedTree = true)
            .onFirst()
            .performClick()

        // 2. Verify that the user card is dismissed
        composeTestRule.onAllNodesWithTag("userCard").onFirst().assertIsDisplayed()
    }
}

/**
 * Helper para completar el flujo de onboarding.
 * Se define como una extensión de ComposeTestRule.
 */
private fun ComposeTestRule.completeOnboarding() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val textNext = context.getString(R.string.next)
    val textStart = context.getString(R.string.start)

    // Avanza por las pantallas de onboarding esperando a que el botón sea visible
    waitUntilNodeWithText(textNext).performClick()
    waitUntilNodeWithText(textNext).performClick()
    waitUntilNodeWithText(textStart).performClick()
}

/**
 * Helper para realizar el login.
 */
private fun ComposeTestRule.login(
    username: String,
    password: String,
) {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val usernameLabel = context.getString(R.string.auth_screen_text_field_username)
    val passwordLabel = context.getString(R.string.auth_screen_text_field_password)
    val loginButtonText = context.getString(R.string.auth_screen_login)
    Log.d("TEST_LOG", "User='$username' | Pass='$passwordLabel'")

    // Usar waitUntilNode para asegurar que la UI está lista antes de interactuar
    waitUntilNodeWithText(usernameLabel).performTextInput(username)
    waitUntilNodeWithText(passwordLabel).performTextInput(password)
    waitUntilNodeWithText(loginButtonText).performClick()
}

/**
 * Helper que espera a que un nodo con un texto específico aparezca en la UI.
 * @return La interacción con el nodo para poder encadenar acciones (ej: .performClick()).
 */
private fun ComposeTestRule.waitUntilNodeWithText(
    text: String,
    timeout: Duration = 5.seconds,
): SemanticsNodeInteraction {
    waitUntil(timeout.inWholeMilliseconds) {
        onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
    }
    return onNodeWithText(text)
}

/**
 * Helper que espera a que un nodo con un testTag específico aparezca en la UI.
 */
private fun ComposeTestRule.waitUntilNodeWithTag(
    tag: String,
    timeout: Duration = 5.seconds,
): SemanticsNodeInteraction {
    waitUntil(timeout.inWholeMilliseconds) {
        onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
    }
    return onNodeWithTag(tag)
}
