/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CreatePrivateChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(otherUserId: String): MiraiLinkResult<String> {
        return try {
            repository.createPrivateChat(otherUserId)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while creating the private chat", e)
        }
    }
}