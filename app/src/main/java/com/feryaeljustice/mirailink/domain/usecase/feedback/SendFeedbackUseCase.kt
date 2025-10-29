/**
 * @author Feryael Justice
 * @since 29/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.feedback

import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class SendFeedbackUseCase @Inject constructor(private val repository: FeedbackRepository) {
    suspend operator fun invoke(feedback: String): MiraiLinkResult<Unit> {
        return try {
            repository.sendFeedback(feedback)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Se produjo un error al enviar el feedback", e)
        }
    }
}