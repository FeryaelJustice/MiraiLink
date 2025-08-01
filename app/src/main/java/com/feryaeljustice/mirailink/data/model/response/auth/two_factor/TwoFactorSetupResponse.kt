package com.feryaeljustice.mirailink.data.model.response.auth.two_factor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwoFactorSetupResponse(
    @SerialName("otpauth_url") val otpAuthUrl: String,
    @SerialName("base32") val baseCode: String,
    @SerialName("recovery_codes") val recoveryCodes: List<String>
)
