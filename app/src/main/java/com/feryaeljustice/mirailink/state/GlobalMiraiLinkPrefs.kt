package com.feryaeljustice.mirailink.state

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GlobalMiraiLinkPrefs(
    private val miraiLinkPrefs: MiraiLinkPrefs,
    private val appScope: CoroutineScope,
) {
    fun markOnboardingCompleted() = appScope.launch { miraiLinkPrefs.markOnboardingCompleted() }

    suspend fun isOnboardingCompleted() = miraiLinkPrefs.isOnboardingCompleted()
}
