package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(private val api: ChatApiService) {
    suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummaryResponse>> {
        return try {
            val response = api.getChatsFromUser()
            MiraiLinkResult.Success(response)
        } catch (e: Exception) {
            Log.e("ChatRemoteDataSource", "getChatsFromUser error", e)
            MiraiLinkResult.Error("Error al obtener los chats", e)
        }
    }

    suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> {
        return try {
            val response = api.createPrivateChat(mapOf("userId" to otherUserId))
            MiraiLinkResult.Success(response.chatId)
        } catch (e: Exception) {
            Log.e("ChatRemoteDataSource", "createPrivateChat error", e)
            MiraiLinkResult.Error("Error al crear el chat privado", e)
        }
    }

    suspend fun createGroupChat(name: String, userIds: List<UserDto>): MiraiLinkResult<String> {
        return try {
            val ids = userIds.map { it.id }
            val body = mapOf("name" to name, "userIds" to ids)
            val response = api.createGroupChat(body)
            MiraiLinkResult.Success(response.chatId)
        } catch (e: Exception) {
            Log.e("ChatRemoteDataSource", "createGroupChat error", e)
            MiraiLinkResult.Error("Error al crear el grupo", e)
        }
    }

    suspend fun sendMessage(toUserId: String, content: String): MiraiLinkResult<Unit> {
        return try {
            api.sendMessage(ChatRequest(toUserId, content))
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error enviar mensaje al chat", e)
        }
    }

    suspend fun getChatHistory(withUserId: String): MiraiLinkResult<List<ChatMessageResponse>> {
        return try {
            val response = api.getChatHistory(withUserId)
            MiraiLinkResult.Success(response)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error al obtener el historial del chat", e)
        }
    }
}