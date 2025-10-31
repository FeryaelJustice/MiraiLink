package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatIdResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApiService {
    @GET("chats")
    suspend fun getChatsFromUser(): List<ChatSummaryResponse>

    @PATCH("chats/{chatId}/read")
    suspend fun markChatAsRead(
        @Path("chatId") chatId: String,
    ): Response<Unit>

    @POST("chats/private")
    suspend fun createPrivateChat(
        @Body body: Map<String, String>,
    ): ChatIdResponse

    @POST("chats/group")
    suspend fun createGroupChat(
        @Body body: CreateGroupChatRequest,
    ): ChatIdResponse

    @POST("chats/send")
    suspend fun sendMessage(
        @Body request: ChatRequest,
    )

    @GET("chats/history/{userId}")
    suspend fun getChatHistory(
        @Path("userId") userId: String,
    ): List<ChatMessageResponse>
}
