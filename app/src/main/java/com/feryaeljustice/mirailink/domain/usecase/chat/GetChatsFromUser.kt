package com.feryaeljustice.mirailink.domain.usecase.chat

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.ChatSummary
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetChatsFromUser @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(): MiraiLinkResult<List<ChatSummary>> {
        val chats = repository.getChatsFromUser()
        Log.d("GetChatsFromUser", "Chats: $chats")
        return chats
    }
}
