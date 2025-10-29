package com.feryaeljustice.mirailink.domain.usecase.onboarding

import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class CheckOnboardingIsCompleted @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> {
        return try {
            repository.checkOnboardingIsCompleted()
        } catch (e: Exception) {
            MiraiLinkResult.Error("CheckOnboardingIsCompleted error: ", e)
        }
    }
}