package com.feryaeljustice.mirailink.domain.usecase.onboarding

import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class CheckOnboardingIsCompleted(
    private val repository: OnboardingRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> =
        try {
            repository.checkOnboardingIsCompleted()
        } catch (e: Exception) {
            MiraiLinkResult.Error("CheckOnboardingIsCompleted error: ", e)
        }
}
