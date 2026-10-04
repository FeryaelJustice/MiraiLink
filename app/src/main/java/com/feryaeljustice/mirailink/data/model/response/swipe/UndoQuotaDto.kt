package com.feryaeljustice.mirailink.data.model.response.swipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UndoQuotaDto(
    @SerialName("tier") val tier: String,
    @SerialName("maxUndos") val maxUndos: Int,
    @SerialName("usedUndos") val usedUndos: Int,
    @SerialName("remainingUndos") val remainingUndos: Int,
    @SerialName("resetsAt") val resetsAt: String? = null,
    @SerialName("hasUndoableSwipe") val hasUndoableSwipe: Boolean = false,
    @SerialName("canUndo") val canUndo: Boolean = false,
)
