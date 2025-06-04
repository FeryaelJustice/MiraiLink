package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import javax.inject.Inject

class MarkChatAsReadUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String) = repository.markChatAsRead(chatId)
}