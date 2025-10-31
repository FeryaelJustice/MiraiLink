package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ChatRemoteDataSource
    @Inject
    constructor(
        private val api: ChatApiService,
    ) {
        suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummaryResponse>> =
            try {
                val response = api.getChatsFromUser()
                MiraiLinkResult.Success(response)
            } catch (e: Throwable) {
                parseMiraiLinkHttpError(e, "ChatRemoteDataSource", "getChatsFromUser")
            }

        suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit> =
            try {
                api.markChatAsRead(chatId)
                MiraiLinkResult.Success(Unit)
            } catch (e: Throwable) {
                parseMiraiLinkHttpError(e, "ChatRemoteDataSource", "markChatAsRead")
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
//                val message = json.optString("message", "Error desconocido")

                    if (!chatId.isNullOrBlank()) {
                        // Devuélvelo como Success porque realmente no es un fallo grave
                        return MiraiLinkResult.Success(chatId)
                    }

                    parseMiraiLinkHttpError(e, "ChatRemoteDataSource", "createPrivateChat")
                } catch (parseError: Exception) {
                    parseMiraiLinkHttpError(
                        e,
                        "ChatRemoteDataSource",
                        "createPrivateChat catch in catch",
                    )
                }
            }
        }

        suspend fun createGroupChat(
            name: String,
            userIds: List<String>,
        ): MiraiLinkResult<String> {
            return try {
                val body = CreateGroupChatRequest(name = name, userIds = userIds)
                val response = api.createGroupChat(body)
                MiraiLinkResult.Success(response.chatId)
            } catch (e: HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    val json = JSONObject(errorBody ?: "{}")

                    val chatId = json.optString("chatId")
                    json.optString("message", "Error desconocido")

                    if (!chatId.isNullOrBlank()) {
                        // Devuélvelo como Success porque realmente no es un fallo grave
                        return MiraiLinkResult.Success(chatId)
                    }

                    parseMiraiLinkHttpError(e, "ChatRemoteDataSource", "createGroupChat")
                } catch (err: Throwable) {
                    parseMiraiLinkHttpError(
                        err,
                        "ChatRemoteDataSource",
                        "createGroupChat catch in catch",
                    )
                }
            }
        }

        suspend fun sendMessage(
            toUserId: String,
            content: String,
        ): MiraiLinkResult<Unit> =
            try {
                api.sendMessage(ChatRequest(toUserId, content))
                MiraiLinkResult.Success(Unit)
            } catch (e: Throwable) {
                parseMiraiLinkHttpError(e, "ChatRemoteDataSource", "sendMessage")
            }

        suspend fun getChatHistory(withUserId: String): MiraiLinkResult<List<ChatMessageResponse>> =
            try {
                val response = api.getChatHistory(withUserId)
                MiraiLinkResult.Success(response)
            } catch (e: Throwable) {
                parseMiraiLinkHttpError(e, "ChatRemoteDataSource", "getChatHistory")
            }
    }
