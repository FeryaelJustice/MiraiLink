package com.feryaeljustice.mirailink.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequest(
    @SerialName("userId") val userId: String,
    @SerialName("type") val type: String
)