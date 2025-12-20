package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ConnectSocketUseCase(
    private val repository: ChatRepository,
) {
    operator fun invoke(): MiraiLinkResult<Unit> =
        try {
            repository.connectSocket()
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while connecting to the socket", e)
        }
}
