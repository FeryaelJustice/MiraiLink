package com.feryaeljustice.mirailink.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("usernameOrEmail")
    val usernameOrEmail: String,
    @SerialName("password")
    val password: String
)