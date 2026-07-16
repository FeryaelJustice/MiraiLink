package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import java.util.concurrent.CancellationException

class OnboardingRepositoryImpl(
    private val miraiLinkPrefs: MiraiLinkPrefs,
) : OnboardingRepository {
    override suspend fun checkOnboardingIsCompleted(): MiraiLinkResult<Boolean> =
        try {
            val res = miraiLinkPrefs.isOnboardingCompleted()
            MiraiLinkResult.success(res)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            MiraiLinkResult.Error(DataError.Local.UNKNOWN)
        }
}
