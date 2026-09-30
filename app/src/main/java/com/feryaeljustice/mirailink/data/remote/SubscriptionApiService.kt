package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.request.subscription.CancelSubscriptionIntentRequest
import com.feryaeljustice.mirailink.data.model.request.subscription.VerifySubscriptionRequest
import com.feryaeljustice.mirailink.data.model.response.subscription.CancelSubscriptionIntentResponse
import com.feryaeljustice.mirailink.data.model.response.subscription.SubscriptionStatusDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SubscriptionApiService {
    @GET("subscription/status")
    suspend fun getSubscriptionStatus(): SubscriptionStatusDto

    @POST("subscription/verify")
    suspend fun verifySubscription(
        @Body request: VerifySubscriptionRequest,
    ): SubscriptionStatusDto

    @POST("subscription/cancel-intent")
    suspend fun cancelSubscriptionIntent(
        @Body request: CancelSubscriptionIntentRequest = CancelSubscriptionIntentRequest(),
    ): CancelSubscriptionIntentResponse
}
