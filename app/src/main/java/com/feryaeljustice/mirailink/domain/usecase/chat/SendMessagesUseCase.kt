/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(userId: String, message: String): MiraiLinkResult<Unit> {
        return try {
            repository.sendMessageTo(userId, message)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while sending the message", e)
        }
    }
}