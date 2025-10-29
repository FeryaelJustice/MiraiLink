/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<List<ChatMessage>> {
        return try {
            repository.getMessagesWith(userId)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting the messages", e)
        }
    }
}