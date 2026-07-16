package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest
import com.feryaeljustice.mirailink.data.remote.FeedbackApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiUnitResponse
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class FeedbackRemoteDatasource(
    private val api: FeedbackApiService,
) {
    suspend fun sendFeedback(feedback: String): MiraiLinkResult<Unit> =
        safeApiUnitResponse(NetworkOperation.AUTHENTICATED) {
            api.sendFeeback(SendFeedbackRequest(feedback))
        }
}
