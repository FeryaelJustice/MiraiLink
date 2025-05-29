package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.ChatMessage
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<List<ChatMessage>> {
        return repository.getMessagesWith(userId)
    }
}