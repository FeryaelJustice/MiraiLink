package com.feryaeljustice.mirailink.domain.usecase.subscription

import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.StateFlow

class GetSubscriptionStatusUseCase(
    private val repository: SubscriptionRepository,
) {
    val subscriptionInfo: StateFlow<SubscriptionPlanInfo> = repository.subscriptionInfo
    val formattedPrice: StateFlow<String?> = repository.formattedPrice

    suspend operator fun invoke(): MiraiLinkResult<SubscriptionPlanInfo> =
        repository.fetchSubscriptionStatus()
}
