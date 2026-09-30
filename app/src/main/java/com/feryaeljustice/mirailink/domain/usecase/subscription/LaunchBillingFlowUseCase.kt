package com.feryaeljustice.mirailink.domain.usecase.subscription

import android.app.Activity
import com.feryaeljustice.mirailink.data.billing.BillingClientManager
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class LaunchBillingFlowUseCase(
    private val repository: SubscriptionRepository,
) {
    operator fun invoke(
        activity: Activity,
        productId: String = BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID,
        basePlanId: String = BillingClientManager.BASE_PLAN_MONTHLY,
    ): MiraiLinkResult<Unit> =
        repository.launchBillingFlow(activity, productId, basePlanId)
}
