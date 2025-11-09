package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource
import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class FeedbackRepositoryImpl(
    private val remote: FeedbackRemoteDatasource,
) : FeedbackRepository {
    override suspend fun sendFeedback(feedback: String): MiraiLinkResult<Unit> = remote.sendFeedback(feedback)
}
