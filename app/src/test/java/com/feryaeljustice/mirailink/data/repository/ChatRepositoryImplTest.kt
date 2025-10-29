/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import android.util.Log
import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class ChatRepositoryImplTest {

    private lateinit var socketService: SocketService
    private lateinit var remoteDataSource: ChatRemoteDataSource
    private lateinit var repository: ChatRepositoryImpl

    private val baseUrl = "http://test.com/"

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        socketService = mockk(relaxUnitFun = true)
        remoteDataSource = mockk()
        repository = ChatRepositoryImpl(socketService, remoteDataSource, baseUrl)
    }

    @Test
    fun `connectSocket calls socketService`() {
        repository.connectSocket()
        coVerify { socketService.connect() }
    }

    @Test
    fun `disconnectSocket calls socketService`() {
        repository.disconnectSocket()
        coVerify { socketService.disconnect() }
    }

    @Test
    fun `getChatsFromUser success returns mapped chats`() = runBlocking {
        // Given
        val chatSummaryResponse = ChatSummaryResponse(
            id = "1",
            type = "private",
            createdBy = "user1",
            createdAt = "2023-01-01T12:00:00.000Z",
            joinedAt = "2023-01-01T12:00:00.000Z",
            role = "member",
            lastMessageId = "msg1",
            lastMessageText = "Hello",
            lastMessageSenderId = "user1",
            lastMessageSentAt = "2023-01-01T12:00:00.000Z",
            unreadCount = "0",
            destinatary = MinimalUserInfoResponse("2", "testuser", "Test User", null)
        )
        val remoteResult = MiraiLinkResult.Success(listOf(chatSummaryResponse))
        coEvery { remoteDataSource.getChatsFromUser() } returns remoteResult

        // When
        val result = repository.getChatsFromUser()

        // Then
        assert(result is MiraiLinkResult.Success)
        val chatSummary = (result as MiraiLinkResult.Success<List<ChatSummary>>).data.first()
        assertEquals("1", chatSummary.id)
        assertEquals("Test User", chatSummary.destinatary?.nickname)
    }

    @Test
    fun `getChatsFromUser with photo url success`() = runBlocking {
        // Given
        val destinataryResponse = MinimalUserInfoResponse("2", "testuser", "Test User", "photo.jpg")
        val chatSummaryResponse = ChatSummaryResponse(
            id = "1",
            type = "private",
            createdBy = "user1",
            createdAt = "2023-01-01T12:00:00.000Z",
            joinedAt = "2023-01-01T12:00:00.000Z",
            role = "member",
            lastMessageId = "msg1",
            lastMessageText = "Hello",
            lastMessageSenderId = "user1",
            lastMessageSentAt = "2023-01-01T12:00:00.000Z",
            unreadCount = "0",
            destinatary = destinataryResponse
        )
        val remoteResult = MiraiLinkResult.Success(listOf(chatSummaryResponse))
        coEvery { remoteDataSource.getChatsFromUser() } returns remoteResult

        // When
        val result = repository.getChatsFromUser()

        // Then
        assert(result is MiraiLinkResult.Success)
        val chatSummary = (result as MiraiLinkResult.Success<List<ChatSummary>>).data.first()
        assertEquals("http://test.com/photo.jpg", chatSummary.destinatary?.profilePhoto?.url)
    }

    @Test
    fun `getMessagesWith success returns mapped messages`() = runBlocking {
        // Given
        val senderDto = UserDto(
            "1",
            "sender",
            "Sender",
            "",
            "",
            "",
            "",
            "",
            emptyList(),
            emptyList(),
            emptyList()
        )
        val receiverDto = UserDto(
            "2",
            "receiver",
            "Receiver",
            "",
            "",
            "",
            "",
            "",
            emptyList(),
            emptyList(),
            emptyList()
        )
        val messageResponse = ChatMessageResponse("1", senderDto, receiverDto, "Hello", Date().time)
        val remoteResult = MiraiLinkResult.Success(listOf(messageResponse))
        coEvery { remoteDataSource.getChatHistory(any()) } returns remoteResult

        // When
        val result = repository.getMessagesWith("2")

        // Then
        assert(result is MiraiLinkResult.Success)
        val message = (result as MiraiLinkResult.Success<List<ChatMessage>>).data.first()
        assertEquals("1", message.id)
        assertEquals("Hello", message.content)
    }

    @Test
    fun `listenForMessages registers callback on socketService`() {
        val callback = slot<((Array<Any>) -> Unit)>()
        coEvery { socketService.on("receive_message", capture(callback)) } answers { nothing }

        repository.listenForMessages { /* testing */ }

        coVerify { socketService.on("receive_message", any()) }
    }
}