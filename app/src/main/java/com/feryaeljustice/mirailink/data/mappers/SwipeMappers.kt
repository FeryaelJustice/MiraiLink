package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.response.swipe.UndoQuotaDto
import com.feryaeljustice.mirailink.data.model.response.swipe.UndoSwipeResponseDto
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.model.swipe.UndoSwipeResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls

fun UndoQuotaDto.toDomain(): UndoQuota {
    val planType = when (tier.lowercase()) {
        "plus" -> SubscriptionPlanType.PLUS
        "premium" -> SubscriptionPlanType.PREMIUM
        else -> SubscriptionPlanType.FREE
    }
    return UndoQuota(
        tier = planType,
        maxUndos = maxUndos,
        usedUndos = usedUndos,
        remainingUndos = remainingUndos,
        resetsAt = resetsAt,
        hasUndoableSwipe = hasUndoableSwipe,
        canUndo = canUndo,
    )
}

fun UndoSwipeResponseDto.toDomain(baseUrl: String): UndoSwipeResult? {
    val userDto = user ?: return null
    val domainUser = userDto.toDomain()
    val orderedPhotos = resolvePhotoUrls(baseUrl, domainUser.photos)
    return UndoSwipeResult(
        user = domainUser.copy(photos = orderedPhotos),
        actionUndone = actionUndone,
        quota = quota.toDomain(),
    )
}
