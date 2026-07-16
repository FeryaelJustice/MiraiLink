package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class MarkChatAsReadUseCase(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(chatId: String): MiraiLinkResult<Unit> =
        repository.markChatAsRead(chatId)
}
