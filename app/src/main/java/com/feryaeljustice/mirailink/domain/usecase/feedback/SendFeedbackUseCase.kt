/**
 * @author Feryael Justice
 * @since 29/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.feedback

import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SendFeedbackUseCase(
    private val repository: FeedbackRepository,
) {
    suspend operator fun invoke(feedback: String): MiraiLinkResult<Unit> =
        try {
            repository.sendFeedback(feedback)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Se produjo un error al enviar el feedback", e)
        }
}
