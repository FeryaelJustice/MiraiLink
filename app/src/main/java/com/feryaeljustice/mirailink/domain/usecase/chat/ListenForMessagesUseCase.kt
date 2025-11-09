package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository

class ListenForMessagesUseCase(
    private val repository: ChatRepository,
) {
    operator fun invoke(callback: (String) -> Unit) {
        repository.listenForMessages(callback)
    }
}
