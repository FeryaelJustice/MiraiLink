package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.ChatMessage
import com.feryaeljustice.mirailink.domain.model.ChatSummary
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface ChatRepository {
    fun connectSocket()
    fun disconnectSocket()
    suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummary>>
    suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit>
    suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String>
    suspend fun createGroupChat(name: String, userIds: List<String>): MiraiLinkResult<String>
    suspend fun getMessagesWith(userId: String): MiraiLinkResult<List<ChatMessage>>
    suspend fun sendMessageTo(userId: String, content: String): MiraiLinkResult<Unit>
    fun listenForMessages(callback: (String) -> Unit)
}