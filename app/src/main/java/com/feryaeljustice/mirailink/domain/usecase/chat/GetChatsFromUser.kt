/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetChatsFromUser @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(): MiraiLinkResult<List<ChatSummary>> {
        return try {
            repository.getChatsFromUser()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting the chats", e)
        }
    }
}