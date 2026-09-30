package com.feryaeljustice.mirailink.domain.usecase.subscription

import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CancelSubscriptionIntentUseCase(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<String> =
        repository.requestCancelIntent()
}
