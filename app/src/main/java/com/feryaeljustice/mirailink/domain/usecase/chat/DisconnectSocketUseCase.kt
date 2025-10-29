/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class DisconnectSocketUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(): MiraiLinkResult<Unit> {
        return try {
            repository.disconnectSocket()
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while disconnecting from the socket", e)
        }
    }
}