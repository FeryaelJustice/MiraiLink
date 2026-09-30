package com.feryaeljustice.mirailink.data.model.request.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CancelSubscriptionIntentRequest(
    @SerialName("reason")
    val reason: String? = null,
)
