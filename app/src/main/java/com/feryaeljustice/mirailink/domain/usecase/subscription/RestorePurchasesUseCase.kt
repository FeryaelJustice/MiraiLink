package com.feryaeljustice.mirailink.domain.usecase.subscription

import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class RestorePurchasesUseCase(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<SubscriptionPlanInfo> =
        repository.restorePurchases()
}
