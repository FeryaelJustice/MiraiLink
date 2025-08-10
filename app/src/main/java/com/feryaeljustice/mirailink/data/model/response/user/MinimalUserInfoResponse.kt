package com.feryaeljustice.mirailink.data.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Represents user info for chat preview and else
@Serializable
data class MinimalUserInfoResponse(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)