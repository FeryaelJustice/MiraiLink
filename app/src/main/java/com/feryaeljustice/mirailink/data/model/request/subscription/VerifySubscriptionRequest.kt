package com.feryaeljustice.mirailink.data.model.request.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifySubscriptionRequest(
    @SerialName("purchaseToken")
    val purchaseToken: String,
    @SerialName("productId")
    val productId: String,
    @SerialName("basePlanId")
    val basePlanId: String = "monthly-autorenew",
    @SerialName("orderId")
    val orderId: String? = null,
)
