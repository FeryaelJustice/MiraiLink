package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest
import com.feryaeljustice.mirailink.data.remote.FeedbackApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class FeedbackRemoteDatasource @Inject constructor(private val api: FeedbackApiService) {
    suspend fun sendFeedback(feedback: String): MiraiLinkResult<Unit> {
        return try {
            val request = SendFeedbackRequest(feedback)
            api.sendFeeback(request)
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("FeedbackRemoteDatasource", "sendFeedback error", e)
            MiraiLinkResult.Error("Error sendFeedback: ${e.message}", e)
        }
    }
}