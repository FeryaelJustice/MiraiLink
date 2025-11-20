// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatIdResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class ChatRemoteDataSourceTest : UnitTest() {

    private val chatApiService: ChatApiService by inject()
    private val chatRemoteDataSource: ChatRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<ChatApiService>() }
                single { ChatRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getChatsFromUser should return list of chat summaries`() = runTest {
        // Given
        val chatSummaries =
            listOf(
                ChatSummaryResponse(
                    id = "1",
                    type = "private",
                    createdBy = "user1",
                    createdAt = "2025-11-01T01:31:32+01:00",
                    joinedAt = "2025-11-01T01:31:32+01:00",
                    role = "member",
                    unreadCount = "0",
                    destinatary = MinimalUserInfoResponse("2", "user2", "User Two"),
                ),
            )
        coEvery { chatApiService.getChatsFromUser() } returns chatSummaries

        // When
        val result = chatRemoteDataSource.getChatsFromUser()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(chatSummaries, (result as MiraiLinkResult.Success).data)
        coVerify { chatApiService.getChatsFromUser() }
    }

    @Test
    fun `markChatAsRead should return success`() = runTest {
        // Given
        val chatId = "1"
        coEvery { chatApiService.markChatAsRead(chatId) } returns Response.success(Unit)

        // When
        val result = chatRemoteDataSource.markChatAsRead(chatId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { chatApiService.markChatAsRead(chatId) }
    }

    @Test
    fun `createPrivateChat should return chat ID on success`() = runTest {
        // Given
        val otherUserId = "user2"
        val response = ChatIdResponse("chat123")
        coEvery { chatApiService.createPrivateChat(mapOf("otherUserId" to otherUserId)) } returns
            response

        // When
        val result = chatRemoteDataSource.createPrivateChat(otherUserId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals("chat123", (result as MiraiLinkResult.Success).data)
        coVerify { chatApiService.createPrivateChat(mapOf("otherUserId" to otherUserId)) }
    }

    @Test
    fun `createPrivateChat should return existing chat ID on 409 conflict`() = runTest {
        // Given
        val otherUserId = "user2"
        val errorBody = "{\"chatId\":\"existingChat456\"}".toResponseBody()
        val exception = HttpException(Response.error<Any>(409, errorBody))
        coEvery { chatApiService.createPrivateChat(mapOf("otherUserId" to otherUserId)) } throws
            exception

        // When
        val result = chatRemoteDataSource.createPrivateChat(otherUserId)

        // Then
        assertTrue(result is MiraiLinkResult.Success) // Handled as success
        assertEquals("existingChat456", (result as MiraiLinkResult.Success).data)
        coVerify { chatApiService.createPrivateChat(mapOf("otherUserId" to otherUserId)) }
    }

    @Test
    fun `createGroupChat should return chat ID on success`() = runTest {
        // Given
        val request = CreateGroupChatRequest("Test Group", listOf("user2", "user3"))
        val response = ChatIdResponse("groupChat789")
        coEvery { chatApiService.createGroupChat(request) } returns response

        // When
        val result = chatRemoteDataSource.createGroupChat(request.name, request.userIds)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals("groupChat789", (result as MiraiLinkResult.Success).data)
        coVerify { chatApiService.createGroupChat(request) }
    }

    @Test
    fun `sendMessage should return success`() = runTest {
        // Given
        val request = ChatRequest("user2", "Hello!")
        coEvery { chatApiService.sendMessage(request) } returns Unit

        // When
        val result = chatRemoteDataSource.sendMessage(request.toUserId, request.text)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { chatApiService.sendMessage(request) }
    }

    @Test
    fun `getChatHistory should return list of messages`() = runTest {
        // Given
        val withUserId = "user2"
        val user1 = UserDto("1", "user1", "User One")
        val user2 = UserDto("2", "user2", "User Two")
        val messages = listOf(ChatMessageResponse("msg1", user1, user2, "Hi", 123L))
        coEvery { chatApiService.getChatHistory(withUserId) } returns messages

        // When
        val result = chatRemoteDataSource.getChatHistory(withUserId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(messages, (result as MiraiLinkResult.Success).data)
        coVerify { chatApiService.getChatHistory(withUserId) }
    }
}
