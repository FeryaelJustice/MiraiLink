package com.feryaeljustice.mirailink.ui.screens.social

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.screens.chat.ChatScreen
import com.feryaeljustice.mirailink.ui.screens.chat.ChatViewModel
import com.feryaeljustice.mirailink.ui.screens.home.HomeScreen
import com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel
import com.feryaeljustice.mirailink.ui.screens.messages.MessagesScreen
import com.feryaeljustice.mirailink.ui.screens.messages.MessagesViewModel
import com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureScreen
import com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureViewModel
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileScreen
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import com.feryaeljustice.mirailink.ui.testing.testSession
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry
import com.google.common.truth.Truth.assertThat
import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Isolated Compose tests for feed, messaging, chat, profile and photo screens. */
@RunWith(AndroidJUnit4::class)
class SocialScreensTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    /** Verifies home renders an empty-success state as user-facing content. */
    @Test
    fun homeScreen_rendersEmptyFeed() {
        val viewModel = mockk<HomeViewModel>(relaxed = true) {
            every { state } returns
                MutableStateFlow<HomeViewModel.HomeUiState>(
                    HomeViewModel.HomeUiState.Success(emptyList(), currentIndex = 0),
                )
            every { canUndo() } returns false
        }
        composeRule.setMiraiLinkContent {
            HomeScreen(testSession(), Modifier.testTag("home-screen"), viewModel)
        }

        composeRule.onNodeWithTag("HomeRefreshBox").assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.users_empty_by_now))
            .assertIsDisplayed()
    }

    /** Verifies the messages screen exposes the AI-chat navigation action. */
    @Test
    fun messagesScreen_opensAiChat() {
        var opened = false
        val viewModel = mockk<MessagesViewModel>(relaxed = true) {
            every { state } returns
                MutableStateFlow<MessagesViewModel.MessagesUiState>(
                    MessagesViewModel.MessagesUiState.Idle,
                )
        }
        composeRule.setMiraiLinkContent {
            MessagesScreen(
                miraiLinkSession = testSession(),
                onNavigateToChat = {},
                onNavigateToAiChat = { opened = true },
                modifier = Modifier.testTag("messages-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithContentDescription(
            composeRule.activity.getString(R.string.ai_chat_open),
        ).performClick()

        composeRule.onNodeWithTag("messages-screen").assertIsDisplayed()
        assertThat(opened).isTrue()
    }

    /** Verifies chat delegates non-blank message submission. */
    @Test
    fun chatScreen_sendsMessage() {
        val viewModel = chatViewModel()
        composeRule.setMiraiLinkContent {
            ChatScreen(
                miraiLinkSession = testSession(),
                userId = "receiver",
                onBackClick = {},
                modifier = Modifier.testTag("chat-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.chat_screen_send_msg))
            .performTextInput("Hello")
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.send))
            .performClick()

        composeRule.onNodeWithTag("chat-screen").assertIsDisplayed()
        verify(exactly = 1) { viewModel.sendMessage("Hello") }
    }

    /** Verifies profile loading state composes with its event channel and edit model. */
    @Test
    fun profileScreen_rendersLoading() {
        val viewModel = mockk<ProfileViewModel>(relaxed = true) {
            every { state } returns
                MutableStateFlow<ProfileViewModel.ProfileUiState>(
                    ProfileViewModel.ProfileUiState.Loading,
                )
            every { editState } returns MutableStateFlow(EditProfileUiState())
            every { editProfUiEvent } returns MutableSharedFlow<EditProfileUiEvent>()
        }
        composeRule.setMiraiLinkContent {
            ProfileScreen(
                miraiLinkSession = testSession(),
                modifier = Modifier.testTag("profile-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithTag("profile-screen").assertIsDisplayed()
    }

    /** Verifies profile-picture screen exposes logout through the shared session. */
    @Test
    fun profilePictureScreen_logsOut() {
        val session = testSession()
        val viewModel = mockk<ProfilePictureViewModel>(relaxed = true) {
            every { uploadSucceeded } returns MutableStateFlow(false)
            every { error } returns MutableStateFlow<UiError?>(null)
        }
        composeRule.setMiraiLinkContent {
            ProfilePictureScreen(
                miraiLinkSession = session,
                onProfileUpload = {},
                modifier = Modifier.testTag("profile-picture-screen"),
                viewModel = viewModel,
            )
        }

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.logout)).performClick()

        composeRule.onNodeWithTag("profile-picture-screen").assertIsDisplayed()
        verify(exactly = 1) { session.clearSession() }
    }

    /** Creates a chat double with concrete empty flows for all collected state. */
    private fun chatViewModel() =
        mockk<ChatViewModel>(relaxed = true) {
            every { chatId } returns MutableStateFlow<String?>(null)
            every { messages } returns MutableStateFlow<List<ChatMessageViewEntry>>(emptyList())
            every { sender } returns MutableStateFlow<MinimalUserInfoViewEntry?>(null)
            every { receiver } returns MutableStateFlow<MinimalUserInfoViewEntry?>(null)
            every { error } returns MutableStateFlow<UiError?>(null)
        }
}
