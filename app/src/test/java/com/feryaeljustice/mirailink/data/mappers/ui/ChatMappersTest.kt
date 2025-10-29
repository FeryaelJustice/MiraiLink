/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class ChatMappersTest {

    @Test
    fun `ChatSummary to ChatPreviewViewEntry with valid avatar url`() {
        // Given
        val validAvatarUrl = "http://example.com/avatar.png"
        val chatSummary = ChatSummary(
            id = "chat1",
            type = ChatType.PRIVATE,
            createdBy = "user1",
            createdAt = Date(),
            joinedAt = Date(),
            role = ChatRole.MEMBER,
            lastMessageText = "Hello World",
            unreadCount = 2,
            destinatary = MinimalUserInfo(
                id = "user2",
                username = "testuser",
                nickname = "Test User",
                profilePhoto = UserPhoto("user2", validAvatarUrl, 1)
            )
        )

        // When
        val viewEntry = chatSummary.toChatPreviewViewEntry()

        // Then
        assertEquals("user2", viewEntry.userId)
        assertEquals("testuser", viewEntry.username)
        assertEquals("Test User", viewEntry.nickname)
        assertEquals(validAvatarUrl, viewEntry.avatarUrl)
        assertEquals("Hello World", viewEntry.lastMessage)
        assertEquals(2, viewEntry.readsPending)
    }

    @Test
    fun `ChatSummary to ChatPreviewViewEntry with invalid avatar url`() {
        // Given
        val invalidAvatarUrl = "invalid_url"
        val chatSummary = ChatSummary(
            id = "chat1",
            type = ChatType.PRIVATE,
            createdBy = "user1",
            createdAt = Date(),
            joinedAt = Date(),
            role = ChatRole.MEMBER,
            lastMessageText = "Hello World",
            unreadCount = 2,
            destinatary = MinimalUserInfo(
                id = "user2",
                username = "testuser",
                nickname = "Test User",
                profilePhoto = UserPhoto("user2", invalidAvatarUrl, 1)
            )
        )

        // When
        val viewEntry = chatSummary.toChatPreviewViewEntry()

        // Then
        assertEquals(TEMPORAL_PLACEHOLDER_PICTURE_URL, viewEntry.avatarUrl)
    }

    @Test
    fun `ChatSummary to ChatPreviewViewEntry with null avatar url`() {
        // Given
        val chatSummary = ChatSummary(
            id = "chat1",
            type = ChatType.PRIVATE,
            createdBy = "user1",
            createdAt = Date(),
            joinedAt = Date(),
            role = ChatRole.MEMBER,
            lastMessageText = "Hello World",
            unreadCount = 2,
            destinatary = MinimalUserInfo(
                id = "user2",
                username = "testuser",
                nickname = "Test User",
                profilePhoto = null
            )
        )

        // When
        val viewEntry = chatSummary.toChatPreviewViewEntry()

        // Then
        assertEquals(TEMPORAL_PLACEHOLDER_PICTURE_URL, viewEntry.avatarUrl)
    }

    @Test
    fun `ChatMessage to ChatMessageViewEntry mapping is correct`() {
        // Given
        val chatMessage = ChatMessage(
            id = "msg1",
            sender = MinimalUserInfo("senderId", "sender", "Sender"),
            receiver = MinimalUserInfo("receiverId", "receiver", "Receiver"),
            content = "Hi there",
            timestamp = System.currentTimeMillis()
        )

        // When
        val viewEntry = chatMessage.toChatMessageViewEntry()

        // Then
        assertEquals("msg1", viewEntry.id)
        assertEquals("senderId", viewEntry.sender.id)
        assertEquals("receiverId", viewEntry.receiver.id)
        assertEquals("Hi there", viewEntry.content)
        assertEquals(chatMessage.timestamp, viewEntry.timestamp)
    }
}
