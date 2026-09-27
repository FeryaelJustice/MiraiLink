package com.feryaeljustice.mirailink.data.model.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String? = null,
    @SerialName("userId")
    val userId: String? = null,
    @SerialName("requires2FA")
    val requires2FA: Boolean = false,
    @SerialName("challengeToken")
    val challengeToken: String? = null,
    @SerialName("expiresIn")
    val expiresIn: Int? = null,
    @SerialName("isVerified")
    val isVerified: Boolean = false,
)