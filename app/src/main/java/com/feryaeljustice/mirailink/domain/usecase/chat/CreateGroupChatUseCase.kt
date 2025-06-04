package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CreateGroupChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(name: String, userIds: List<String>): MiraiLinkResult<String> {
        return repository.createGroupChat(name = name, userIds = userIds)
    }
}