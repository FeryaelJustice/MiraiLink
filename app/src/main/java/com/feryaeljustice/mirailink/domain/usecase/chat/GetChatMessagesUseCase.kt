package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetChatMessagesUseCase(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<List<ChatMessage>> =
        repository.getMessagesWith(userId)
}
