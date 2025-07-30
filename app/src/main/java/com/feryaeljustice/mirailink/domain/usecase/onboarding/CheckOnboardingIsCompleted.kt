package com.feryaeljustice.mirailink.domain.usecase.onboarding

import android.util.Log
import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CheckOnboardingIsCompleted @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> {
        return try {
            Log.d("CheckOnboardingIsCompleted", "Trying to CheckOnboardingIsCompleted")
            repository.checkOnboardingIsCompleted()
        } catch (e: Exception) {
            MiraiLinkResult.Error("CheckOnboardingIsCompleted error: ", e)
        }
    }
}