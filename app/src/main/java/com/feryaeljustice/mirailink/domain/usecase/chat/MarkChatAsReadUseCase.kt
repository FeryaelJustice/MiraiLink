/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class MarkChatAsReadUseCase(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(chatId: String): MiraiLinkResult<Unit> =
        try {
            repository.markChatAsRead(chatId)
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while marking the chat as read", e)
        }
}
