package com.feryaeljustice.mirailink.ui.testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

/** Renders isolated screen content with the production Material theme. */
internal fun ComposeContentTestRule.setMiraiLinkContent(content: @Composable () -> Unit) {
    setContent {
        MiraiLinkTheme(dynamicColor = false, content = content)
    }
}

/**
 * Creates a relaxed session double with real observable flow values.
 *
 * Screens may call bar or session commands freely while tests retain deterministic identity state.
 */
internal fun testSession(userId: String? = "test-user"): GlobalMiraiLinkSession =
    mockk<GlobalMiraiLinkSession>(relaxed = true) {
        every { currentUserId } returns MutableStateFlow(userId)
        every { isAuthenticated } returns MutableStateFlow(userId != null)
        every { isVerified } returns MutableStateFlow(false)
        every { hasProfilePicture } returns MutableStateFlow<Boolean?>(null)
    }
