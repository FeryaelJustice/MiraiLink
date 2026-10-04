package com.feryaeljustice.mirailink.domain.model.swipe

import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType

data class UndoQuota(
    val tier: SubscriptionPlanType,
    val maxUndos: Int,
    val usedUndos: Int,
    val remainingUndos: Int,
    val resetsAt: String?,
    val hasUndoableSwipe: Boolean,
    val canUndo: Boolean,
)
