package com.feryaeljustice.mirailink.data.repository.delegating

import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DelegatingChatRepository(
    private val remoteRepo: ChatRepository,
    private val demoRepo: ChatRepository,
    private val demoModeManager: DemoModeManager,
) : ChatRepository {

    private fun targetRepo(): ChatRepository =
        if (demoModeManager.isDemoActive()) demoRepo else remoteRepo

    override fun connectSocket() = targetRepo().connectSocket()

    override fun disconnectSocket() = targetRepo().disconnectSocket()

    override suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummary>> =
        targetRepo().getChatsFromUser()

    override suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit> =
        targetRepo().markChatAsRead(chatId)

    override suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> =
        targetRepo().createPrivateChat(otherUserId)

    override suspend fun createGroupChat(
        name: String,
        userIds: List<String>,
    ): MiraiLinkResult<String> = targetRepo().createGroupChat(name, userIds)

    override suspend fun getMessagesWith(userId: String): MiraiLinkResult<List<ChatMessage>> =
        targetRepo().getMessagesWith(userId)

    override suspend fun sendMessageTo(
        userId: String,
        content: String,
    ): MiraiLinkResult<Unit> = targetRepo().sendMessageTo(userId, content)

    override fun listenForMessages(callback: (String) -> Unit) =
        targetRepo().listenForMessages(callback)
}
