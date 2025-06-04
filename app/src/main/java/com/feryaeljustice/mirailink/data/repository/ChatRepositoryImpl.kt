package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.domain.model.ChatMessage
import com.feryaeljustice.mirailink.domain.model.ChatSummary
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val socketService: SocketService,
    private val remote: ChatRemoteDataSource
) : ChatRepository {

    override fun connectSocket() = socketService.connect()

    override fun disconnectSocket() = socketService.disconnect()

    override suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummary>> {
        return when (val result = remote.getChatsFromUser()) {
            is MiraiLinkResult.Success -> {
                val chatSummaries = result.data.map { chatSummary ->
                    chatSummary.toDomain()
                }
                MiraiLinkResult.Success(chatSummaries)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit>{
        return remote.markChatAsRead(chatId)
    }

    override suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> {
        return remote.createPrivateChat(otherUserId)
    }

    override suspend fun createGroupChat(
        name: String,
        userIds: List<String>
    ): MiraiLinkResult<String> {
        return remote.createGroupChat(name, userIds)
    }

    override suspend fun sendMessageTo(userId: String, content: String): MiraiLinkResult<Unit> {
        return when (val result = remote.sendMessage(userId, content)) {
            is MiraiLinkResult.Success -> {
                MiraiLinkResult.Success(Unit)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun getMessagesWith(userId: String): MiraiLinkResult<List<ChatMessage>> {
        return when (val result = remote.getChatHistory(userId)) {
            is MiraiLinkResult.Success -> {
                val messages = result.data.distinctBy { it.id }.map { message ->
                    message.toDomain()
                }
                MiraiLinkResult.Success(messages)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override fun listenForMessages(callback: (String) -> Unit) {
        socketService.on("receive_message") { args ->
            val msg = args.firstOrNull()?.toString() ?: return@on
            callback(msg)
        }
    }
}
