package com.feryaeljustice.mirailink.data.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Represents user info for chat preview and else
@Serializable
data class MinimalUserInfo(
    @SerialName("id") val id: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)