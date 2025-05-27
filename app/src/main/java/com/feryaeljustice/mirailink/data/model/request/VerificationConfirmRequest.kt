package com.feryaeljustice.mirailink.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationConfirmRequest(
    @SerialName("userId")
    val userId: String,
    @SerialName("token")
    val token: String,
    @SerialName("type")
    val type: String
)