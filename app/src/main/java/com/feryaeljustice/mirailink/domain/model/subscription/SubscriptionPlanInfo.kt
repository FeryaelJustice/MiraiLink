package com.feryaeljustice.mirailink.domain.model.subscription

enum class SubscriptionDuration {
    WEEKLY,
    MONTHLY,
    THREE_MONTHS,
}

data class SubscriptionOfferOption(
    val duration: SubscriptionDuration,
    val productId: String,
    val basePlanId: String,
    val formattedPrice: String,
    val formattedPricePerPeriod: String? = null,
    val discountBadge: String? = null,
    val offerToken: String = "",
)

data class SubscriptionPlanInfo(
    val planType: SubscriptionPlanType = SubscriptionPlanType.FREE,
    val isPremium: Boolean = false,
    val isPlus: Boolean = false,
    val status: String = "free",
    val productId: String? = null,
    val basePlanId: String? = null,
    val formattedPrice: String? = null,
    val billingPeriod: String? = null,
    val expiresAt: String? = null,
    val autoRenewing: Boolean = false,
)
