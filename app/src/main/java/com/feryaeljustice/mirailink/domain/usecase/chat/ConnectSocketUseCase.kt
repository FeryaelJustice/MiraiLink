package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import javax.inject.Inject

class ConnectSocketUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke() = repository.connectSocket()
}
