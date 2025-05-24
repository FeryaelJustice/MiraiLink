package com.feryaeljustice.mirailink.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {

    @POST("chats/private")
    suspend fun createPrivateChat(@Body body: Map<String, String>): ChatIdResponse

    @POST("chats/group")
    suspend fun createGroupChat(@Body body: Map<String, Any>): ChatIdResponse
}

data class ChatIdResponse(val chatId: String)