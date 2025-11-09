package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrl

class ChatRepositoryImpl(
    private val socketService: SocketService,
    private val remote: ChatRemoteDataSource,
    private val baseUrl: String,
) : ChatRepository {
    override fun connectSocket() = socketService.connect()

    override fun disconnectSocket() = socketService.disconnect()

    override suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummary>> =
        when (val result = remote.getChatsFromUser()) {
            is MiraiLinkResult.Success -> {
                val chatSummaries =
                    result.data.map { chatSummary ->
                        val domain = chatSummary.toDomain()
                        val updatedDestinatary =
                            domain.destinatary?.copy(
                                profilePhoto =
                                    domain.destinatary.profilePhoto?.copy(
                                        url =
                                            resolvePhotoUrl(
                                                baseUrl,
                                                domain.destinatary.profilePhoto.url,
                                            ),
                                    ),
                            )
                        domain.copy(destinatary = updatedDestinatary)
                    }
                MiraiLinkResult.Success(chatSummaries)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit> = remote.markChatAsRead(chatId)

    override suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> = remote.createPrivateChat(otherUserId)

    override suspend fun createGroupChat(
        name: String,
        userIds: List<String>,
    ): MiraiLinkResult<String> = remote.createGroupChat(name, userIds)

    override suspend fun sendMessageTo(
        userId: String,
        content: String,
    ): MiraiLinkResult<Unit> =
        when (val result = remote.sendMessage(userId, content)) {
            is MiraiLinkResult.Success -> {
                MiraiLinkResult.Success(Unit)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun getMessagesWith(userId: String): MiraiLinkResult<List<ChatMessage>> =
        when (val result = remote.getChatHistory(userId)) {
            is MiraiLinkResult.Success -> {
                val messages =
                    result.data.distinctBy { it.id }.map { message ->
                        message.toDomain()
                    }
                MiraiLinkResult.Success(messages)
            }

            is MiraiLinkResult.Error -> result
        }

    override fun listenForMessages(callback: (String) -> Unit) {
        socketService.on("receive_message") { args ->
            val msg = args.firstOrNull()?.toString() ?: return@on
            callback(msg)
        }
    }
}
