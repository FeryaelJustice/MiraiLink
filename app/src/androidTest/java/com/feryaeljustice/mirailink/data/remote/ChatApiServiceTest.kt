// Feryael Justice
// 2025-11-08

package com.feryaeljustice.mirailink.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatIdResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChatApiServiceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var chatApiService: ChatApiService
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        hiltRule.inject()

        val okHttp =
            OkHttpClient
                .Builder()
                .build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(okHttp)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

        chatApiService = retrofit.create(ChatApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getChatsFromUser_returnsSuccess() =
        runBlocking {
            // Given
            val chatSummary =
                ChatSummaryResponse(
                    id = "1",
                    type = "private",
                    createdBy = "user1",
                    createdAt = "2024-01-01T12:00:00Z",
                    joinedAt = "2024-01-01T12:00:00Z",
                    role = "member",
                    lastMessageId = "msg1",
                    lastMessageText = "Hello",
                    lastMessageSenderId = "user2",
                    lastMessageSentAt = "2024-01-01T12:01:00Z",
                    unreadCount = "1",
                    destinatary = MinimalUserInfoResponse("user2", "user2", "User Two", null),
                )
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(listOf(chatSummary)))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = chatApiService.getChatsFromUser()

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(1)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/chats")
            assertThat(request.method).isEqualTo("GET")
        }

    @Test
    fun markChatAsRead_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(204) // No content
            mockWebServer.enqueue(mockResponse)

            // When
            val response = chatApiService.markChatAsRead("1")

            // Then
            assertThat(response.isSuccessful).isTrue()
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/chats/1/read")
            assertThat(request.method).isEqualTo("PATCH")
        }

    @Test
    fun createPrivateChat_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = ChatIdResponse(chatId = "new-chat-id")
            val mockResponse =
                MockResponse()
                    .setResponseCode(201)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)
            val requestBody = mapOf("destinataryId" to "user2")

            // When
            val response = chatApiService.createPrivateChat(requestBody)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.chatId).isEqualTo("new-chat-id")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/chats/private")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun createGroupChat_returnsSuccess() =
        runBlocking {
            // Given
            val expectedResponse = ChatIdResponse(chatId = "new-group-chat-id")
            val mockResponse =
                MockResponse()
                    .setResponseCode(201)
                    .setBody(Json.encodeToString(expectedResponse))
            mockWebServer.enqueue(mockResponse)

            val requestBody =
                CreateGroupChatRequest(
                    name = "New Group",
                    userIds = listOf("user2", "user3"),
                )

            // When
            val response = chatApiService.createGroupChat(requestBody)

            // Then
            assertThat(response).isNotNull()
            assertThat(response.chatId).isEqualTo("new-group-chat-id")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/chats/group")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun sendMessage_returnsSuccess() =
        runBlocking {
            // Given
            val mockResponse = MockResponse().setResponseCode(200)
            mockWebServer.enqueue(mockResponse)
            val chatRequest = ChatRequest(toUserId = "user2", text = "Hello")

            // When
            chatApiService.sendMessage(chatRequest)

            // Then
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/chats/send")
            assertThat(request.method).isEqualTo("POST")
        }

    @Test
    fun getChatHistory_returnsSuccess() =
        runBlocking {
            // Given
            val user1 = UserDto(id = "1", username = "user1", nickname = "User One")
            val user2 = UserDto(id = "2", username = "user2", nickname = "User Two")
            val chatHistory =
                listOf(
                    ChatMessageResponse("1", user1, user2, "Hello", System.currentTimeMillis()),
                    ChatMessageResponse("2", user2, user1, "Hi", System.currentTimeMillis()),
                )
            val mockResponse =
                MockResponse()
                    .setResponseCode(200)
                    .setBody(Json.encodeToString(chatHistory))
            mockWebServer.enqueue(mockResponse)

            // When
            val response = chatApiService.getChatHistory("2")

            // Then
            assertThat(response).isNotNull()
            assertThat(response).hasSize(2)
            assertThat(response[0].id).isEqualTo("1")
            val request = mockWebServer.takeRequest()
            assertThat(request.path).isEqualTo("/chats/history/2")
            assertThat(request.method).isEqualTo("GET")
        }
}
