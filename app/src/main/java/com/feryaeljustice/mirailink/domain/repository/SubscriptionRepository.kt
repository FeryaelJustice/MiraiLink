package com.feryaeljustice.mirailink.domain.repository

import android.app.Activity
import com.feryaeljustice.mirailink.data.billing.BillingClientManager
import com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SubscriptionRepository {
    val subscriptionInfo: StateFlow<SubscriptionPlanInfo>
    val purchaseEvents: Flow<BillingPurchaseEvent>
    val formattedPrice: StateFlow<String?>
    val availableOffers: StateFlow<Map<SubscriptionPlanType, List<SubscriptionOfferOption>>>

    suspend fun fetchSubscriptionStatus(): MiraiLinkResult<SubscriptionPlanInfo>
    fun launchBillingFlow(
        activity: Activity,
        productId: String = BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID,
        basePlanId: String = BillingClientManager.BASE_PLAN_MONTHLY,
    ): MiraiLinkResult<Unit>
    suspend fun verifyPurchase(
        purchaseToken: String,
        productId: String,
        basePlanId: String = BillingClientManager.BASE_PLAN_MONTHLY,
        orderId: String? = null,
    ): MiraiLinkResult<SubscriptionPlanInfo>
    suspend fun restorePurchases(): MiraiLinkResult<SubscriptionPlanInfo>
    suspend fun requestCancelIntent(): MiraiLinkResult<String>
}
