package com.feryaeljustice.mirailink.domain.model.auth

data class AuthSessionInfo(
    val token: String? = null,
    val userId: String? = null,
    val requires2FA: Boolean = false,
    val challengeToken: String? = null,
    val expiresIn: Int? = null,
    val isVerified: Boolean = false,
)
