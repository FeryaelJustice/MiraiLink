package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CreatePrivateChatUseCase(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(otherUserId: String): MiraiLinkResult<String> =
        try {
            repository.createPrivateChat(otherUserId)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while creating the private chat", e)
        }
}
