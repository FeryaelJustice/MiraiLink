package com.feryaeljustice.mirailink.data.model.request.swipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UndoSwipeRequest(
    @SerialName("targetUserId") val targetUserId: String? = null,
)
