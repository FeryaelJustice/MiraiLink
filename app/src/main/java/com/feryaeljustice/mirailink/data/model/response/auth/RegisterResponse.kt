package com.feryaeljustice.mirailink.data.model.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("message")
    val message: String,
    @SerialName("token")
    val token: String
)