package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.data.mappers.toModel
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CreateGroupChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(name: String, userIds: List<User>): MiraiLinkResult<String> {
        return repository.createGroupChat(name, userIds = userIds.map { it.toModel() })
    }
}