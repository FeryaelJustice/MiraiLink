package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.response.chat.ChatIdResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {

    @GET("chats")
    suspend fun getChatsFromUser(): List<ChatSummaryResponse>

    @POST("chats/private")
    suspend fun createPrivateChat(@Body body: Map<String, String>): ChatIdResponse

    @POST("chats/group")
    suspend fun createGroupChat(@Body body: Map<String, Any>): ChatIdResponse
}