package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.domain.model.ChatSummary
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface ChatRepository {
    fun connectSocket()
    fun disconnectSocket()
    suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummary>>
    suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String>
    suspend fun createGroupChat(name: String, userIds: List<UserDto>): MiraiLinkResult<String>
    fun sendMessage(message: String)
    fun listenForMessages(callback: (String) -> Unit)
}