package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface OnboardingRepository {
    suspend fun checkOnboardingIsCompleted(): MiraiLinkResult<Boolean>
}