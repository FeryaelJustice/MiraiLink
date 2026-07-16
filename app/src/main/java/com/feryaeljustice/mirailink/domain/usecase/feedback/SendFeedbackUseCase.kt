package com.feryaeljustice.mirailink.domain.usecase.feedback

import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SendFeedbackUseCase(
    private val repository: FeedbackRepository,
) {
    suspend operator fun invoke(feedback: String): MiraiLinkResult<Unit> =
        repository.sendFeedback(feedback)
}
