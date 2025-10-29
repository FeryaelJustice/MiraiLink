/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class ChatMapperTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @Test
    fun `ChatSummaryResponse with all fields maps correctly to ChatSummary`() {
        // Given
        val response = ChatSummaryResponse(
            id = "chat1",
            type = "private",
            createdBy = "user1",
            createdAt = dateFormat.format(Date()),
            joinedAt = dateFormat.format(Date()),
            role = "member",
            lastMessageId = "msg1",
            lastMessageText = "Hello",
            lastMessageSenderId = "user2",
            lastMessageSentAt = dateFormat.format(Date()),
            unreadCount = "5",
            destinatary = MinimalUserInfoResponse(
                id = "user2",
                username = "testuser",
                nickname = "Test",
                avatarUrl = "url"
            )
        )

        // When
        val domain = response.toDomain()

        // Then
        assertEquals("chat1", domain.id)
        assertEquals(ChatType.PRIVATE, domain.type)
        assertEquals("user1", domain.createdBy)
        assertNotNull(domain.createdAt)
        assertNotNull(domain.joinedAt)
        assertEquals(ChatRole.MEMBER, domain.role)
        assertEquals("msg1", domain.lastMessageId)
        assertEquals("Hello", domain.lastMessageText)
        assertEquals("user2", domain.lastMessageSenderId)
        assertNotNull(domain.lastMessageSentAt)
        assertEquals(5, domain.unreadCount)
        assertNotNull(domain.destinatary)
        assertEquals("user2", domain.destinatary?.id)
    }

    @Test
    fun `ChatSummaryResponse with nullable fields maps correctly`() {
        // Given
        val response = ChatSummaryResponse(
            id = "chat2",
            type = "group",
            createdBy = "user1",
            createdAt = dateFormat.format(Date()),
            joinedAt = dateFormat.format(Date()),
            role = "admin",
            lastMessageId = null,
            lastMessageText = null,
            lastMessageSenderId = null,
            lastMessageSentAt = null,
            unreadCount = "0",
            destinatary = null
        )

        // When
        val domain = response.toDomain()

        // Then
        assertEquals("chat2", domain.id)
        assertEquals(ChatType.GROUP, domain.type)
        assertEquals(ChatRole.ADMIN, domain.role)
        assertNull(domain.lastMessageId)
        assertEquals("", domain.lastMessageText)
        assertNull(domain.lastMessageSenderId)
        assertNotNull(domain.lastMessageSentAt) // Falls back to createdAt
        assertEquals(0, domain.unreadCount)
        assertNull(domain.destinatary)
    }

    @Test
    fun `ChatMessageResponse maps correctly to ChatMessage`() {
        // Given
        val senderDto = UserDto("senderId", "sender", "Sender")
        val receiverDto = UserDto("receiverId", "receiver", "Receiver")
        val response = ChatMessageResponse(
            id = "msg1",
            sender = senderDto,
            receiver = receiverDto,
            content = "Hi there",
            timestamp = System.currentTimeMillis()
        )

        // When
        val domain = response.toDomain()

        // Then
        assertEquals("msg1", domain.id)
        assertEquals("senderId", domain.sender.id)
        assertEquals("receiverId", domain.receiver.id)
        assertEquals("Hi there", domain.content)
        assertEquals(response.timestamp, domain.timestamp)
    }
}
