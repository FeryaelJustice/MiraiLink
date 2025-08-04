package com.feryaeljustice.mirailink.domain.model.auth

data class TwoFactorAuthInfo(
    val enabled: Boolean? = false,
    val otpAuthUrl: String? = null,
    val baseCode: String? = null,
    val recoveryCodes: List<String>? = emptyList()
)
