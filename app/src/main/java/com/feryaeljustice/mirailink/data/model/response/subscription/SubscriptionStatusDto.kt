package com.feryaeljustice.mirailink.data.model.response.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionStatusDto(
    @SerialName("isPremium")
    val isPremium: Boolean = false,
    @SerialName("isPlus")
    val isPlus: Boolean = false,
    @SerialName("plan")
    val plan: String = "free",
    @SerialName("status")
    val status: String = "free",
    @SerialName("productId")
    val productId: String? = null,
    @SerialName("basePlanId")
    val basePlanId: String? = null,
    @SerialName("expiresAt")
    val expiresAt: String? = null,
    @SerialName("autoRenewing")
    val autoRenewing: Boolean = false,
)
