/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CreateGroupChatUseCase(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(
        name: String,
        userIds: List<String>,
    ): MiraiLinkResult<String> =
        try {
            repository.createGroupChat(name = name, userIds = userIds)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while creating the group chat", e)
        }
}
