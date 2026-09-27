package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo

fun LoginResponse.toAuthSessionInfo(): AuthSessionInfo = AuthSessionInfo(
    token = token,
    userId = userId,
    requires2FA = requires2FA,
    challengeToken = challengeToken,
    expiresIn = expiresIn,
    isVerified = isVerified,
)
