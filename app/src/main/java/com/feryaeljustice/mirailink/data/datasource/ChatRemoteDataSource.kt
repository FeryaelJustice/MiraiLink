package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(private val api: ChatApiService) {
    suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummaryResponse>> {
        return try {
            val response = api.getChatsFromUser()
            MiraiLinkResult.Success(response)
        } catch (e: Exception) {
            Log.e("ChatRemoteDataSource", "getChatsFromUser error", e)
            MiraiLinkResult.Error("Error al obtener los chats: ${e.message}", e)
        }
    }

    suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit> {
        return try {
            api.markChatAsRead(chatId)
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("ChatRemoteDataSource", "markChatAsRead error", e)
            MiraiLinkResult.Error("Error al marcar el chat como leído: ${e.message}", e)
        }
    }

    suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> {
        return try {
            val response = api.createPrivateChat(mapOf("otherUserId" to otherUserId))
            MiraiLinkResult.Success(response.chatId)
        } catch (e: HttpException) {
            try {
                val errorBody = e.response()?.errorBody()?.string()
                val json = JSONObject(errorBody ?: "{}")

                val chatId = json.optString("chatId")
                val message = json.optString("message", "Error desconocido")

                if (!chatId.isNullOrBlank()) {
                    // Devuélvelo como Success porque realmente no es un fallo grave
                    return MiraiLinkResult.Success(chatId)
                }

                return MiraiLinkResult.Error(message, e)
            } catch (parseError: Exception) {
                return MiraiLinkResult.Error(
                    "Error al interpretar respuesta de error: ${e.message}",
                    parseError
                )
            }
        }
    }

    suspend fun createGroupChat(name: String, userIds: List<String>): MiraiLinkResult<String> {
        return try {
            val ids = userIds
            val body = mapOf("name" to name, "userIds" to ids)
            val response = api.createGroupChat(body)
            MiraiLinkResult.Success(response.chatId)
        } catch (e: HttpException) {
            try {
                val errorBody = e.response()?.errorBody()?.string()
                val json = JSONObject(errorBody ?: "{}")

                val chatId = json.optString("chatId")
                val message = json.optString("message", "Error desconocido")

                if (!chatId.isNullOrBlank()) {
                    // Devuélvelo como Success porque realmente no es un fallo grave
                    return MiraiLinkResult.Success(chatId)
                }

                return MiraiLinkResult.Error(message, e)
            } catch (parseError: Exception) {
                return MiraiLinkResult.Error(
                    "Error al interpretar respuesta de error: ${e.message}",
                    parseError
                )
            }
        }
    }

    suspend fun sendMessage(toUserId: String, content: String): MiraiLinkResult<Unit> {
        return try {
            api.sendMessage(ChatRequest(toUserId, content))
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error enviar mensaje al chat: ${e.message}", e)
        }
    }

    suspend fun getChatHistory(withUserId: String): MiraiLinkResult<List<ChatMessageResponse>> {
        return try {
            val response = api.getChatHistory(withUserId)
            MiraiLinkResult.Success(response)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error al obtener el historial del chat: ${e.message}", e)
        }
    }
}