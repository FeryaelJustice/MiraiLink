package com.feryaeljustice.mirailink.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Represents user info for chat preview and else
@Serializable
data class MinimalUserInfo(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)
