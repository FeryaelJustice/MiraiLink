package com.feryaeljustice.mirailink.state

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GlobalMiraiLinkPrefs
    @Inject
    constructor(
        private val miraiLinkPrefs: MiraiLinkPrefs,
        @param:ApplicationScope
        private val appScope: CoroutineScope,
    ) {
        fun markOnboardingCompleted() = appScope.launch { miraiLinkPrefs.markOnboardingCompleted() }

        suspend fun isOnboardingCompleted() = miraiLinkPrefs.isOnboardingCompleted()
    }
