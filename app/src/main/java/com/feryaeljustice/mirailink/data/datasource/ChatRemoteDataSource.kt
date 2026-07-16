package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.data.util.NetworkErrorMapper
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.data.util.safeApiCallRecoveringHttp
import com.feryaeljustice.mirailink.data.util.safeApiUnitResponse
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ChatRemoteDataSource(
    private val api: ChatApiService,
) {
    suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummaryResponse>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getChatsFromUser()
        }

    suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit> =
        safeApiUnitResponse(NetworkOperation.AUTHENTICATED) {
            api.markChatAsRead(chatId)
        }

    suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> =
        safeApiCallRecoveringHttp(
            operation = NetworkOperation.AUTHENTICATED,
            recover = { exception ->
                if (exception.code() == 409) {
                    NetworkErrorMapper.existingChatId(exception)
                } else {
                    null
                }
            },
        ) {
            api.createPrivateChat(mapOf("otherUserId" to otherUserId)).chatId
        }

    suspend fun createGroupChat(
        name: String,
        userIds: List<String>,
    ): MiraiLinkResult<String> =
        safeApiCallRecoveringHttp(
            operation = NetworkOperation.AUTHENTICATED,
            recover = { exception ->
                if (exception.code() == 409) {
                    NetworkErrorMapper.existingChatId(exception)
                } else {
                    null
                }
            },
        ) {
            api.createGroupChat(CreateGroupChatRequest(name = name, userIds = userIds)).chatId
        }

    suspend fun sendMessage(
        toUserId: String,
        content: String,
    ): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.sendMessage(ChatRequest(toUserId, content))
        }

    suspend fun getChatHistory(withUserId: String): MiraiLinkResult<List<ChatMessageResponse>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getChatHistory(withUserId)
        }
}
