package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import java.util.concurrent.CancellationException

class DisconnectSocketUseCase(
    private val repository: ChatRepository,
) {
    operator fun invoke(): MiraiLinkResult<Unit> =
        try {
            repository.disconnectSocket()
            MiraiLinkResult.Success(Unit)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (e: Exception) {
            MiraiLinkResult.Error(DataError.Network.UNKNOWN)
        }
}
