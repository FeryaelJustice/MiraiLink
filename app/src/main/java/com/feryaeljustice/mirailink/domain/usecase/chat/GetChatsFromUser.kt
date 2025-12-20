package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetChatsFromUser(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<List<ChatSummary>> =
        try {
            repository.getChatsFromUser()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting the chats", e)
        }
}
