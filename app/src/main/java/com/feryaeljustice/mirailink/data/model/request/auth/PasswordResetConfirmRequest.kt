package com.feryaeljustice.mirailink.data.model.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetConfirmRequest(
    @SerialName("email")
    val email: String,
    @SerialName("token")
    val token: String,
    @SerialName("newPassword")
    val newPassword: String
)