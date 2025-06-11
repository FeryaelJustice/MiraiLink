package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackApiService {
    @POST("feedback")
    suspend fun sendFeeback(@Body request: SendFeedbackRequest): Response<Unit>
}