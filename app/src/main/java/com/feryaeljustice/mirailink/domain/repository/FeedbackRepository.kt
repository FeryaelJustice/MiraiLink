package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface FeedbackRepository {
    suspend fun sendFeedback(feedback: String): MiraiLinkResult<Unit>
}