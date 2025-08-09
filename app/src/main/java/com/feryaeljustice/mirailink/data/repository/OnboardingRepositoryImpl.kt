package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(private val miraiLinkPrefs: MiraiLinkPrefs) :
    OnboardingRepository {
    override suspend fun checkOnboardingIsCompleted(): MiraiLinkResult<Boolean> {
        return try {
            val res = miraiLinkPrefs.isOnboardingCompleted()
            MiraiLinkResult.success(res)
        } catch (e: Throwable) {
            MiraiLinkResult.Error("Error desconocido en checkOnboardingIsCompleted repository", e)
        }
    }
}