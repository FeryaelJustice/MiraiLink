@file:Suppress("ktlint:standard:filename")

package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SendMessageUseCase(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(
        userId: String,
        message: String,
    ): MiraiLinkResult<Unit> =
        try {
            repository.sendMessageTo(userId, message)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while sending the message", e)
        }
}
