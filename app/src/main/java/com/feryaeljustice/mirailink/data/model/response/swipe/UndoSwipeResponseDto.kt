package com.feryaeljustice.mirailink.data.model.response.swipe

import com.feryaeljustice.mirailink.data.model.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UndoSwipeResponseDto(
    @SerialName("message") val message: String,
    @SerialName("actionUndone") val actionUndone: String,
    @SerialName("user") val user: UserDto? = null,
    @SerialName("quota") val quota: UndoQuotaDto,
)
