package com.feryaeljustice.mirailink.data.model.request.auth

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class LoginRequest(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("email")
    val email: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("username")
    val username: String? = null,
    @SerialName("password")
    val password: String,
)