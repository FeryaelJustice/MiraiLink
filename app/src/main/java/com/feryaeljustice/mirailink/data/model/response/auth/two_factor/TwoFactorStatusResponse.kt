package com.feryaeljustice.mirailink.data.model.response.auth.two_factor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwoFactorStatusResponse(
    @SerialName("enabled") val enabled: Boolean,
)
