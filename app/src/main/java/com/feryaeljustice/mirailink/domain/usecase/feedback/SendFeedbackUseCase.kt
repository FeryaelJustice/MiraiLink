package com.feryaeljustice.mirailink.domain.usecase.feedback

import android.util.Log
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class SendFeedbackUseCase @Inject constructor() {
    operator fun invoke(feedback: String): MiraiLinkResult<Unit> {
        return try {
            Log.d("FeedbackUseCase", "Sending feedback: $feedback")
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Se produjo un error al enviar el feedback", e)
        }
    }
}