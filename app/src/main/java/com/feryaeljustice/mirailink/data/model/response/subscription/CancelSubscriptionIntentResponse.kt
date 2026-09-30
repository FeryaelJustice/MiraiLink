package com.feryaeljustice.mirailink.data.model.response.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CancelSubscriptionIntentResponse(
    @SerialName("message")
    val message: String,
    @SerialName("playStoreUrl")
    val playStoreUrl: String,
)
