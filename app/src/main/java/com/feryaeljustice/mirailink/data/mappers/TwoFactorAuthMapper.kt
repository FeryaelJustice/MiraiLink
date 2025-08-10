package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse
import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo

fun TwoFactorSetupResponse.toTwoFactorAuthInfo(): TwoFactorAuthInfo = TwoFactorAuthInfo(
    otpAuthUrl = otpAuthUrl,
    baseCode = baseCode,
    recoveryCodes = recoveryCodes
)

fun TwoFactorStatusResponse.toTwoFactorAuthInfo(): TwoFactorAuthInfo = TwoFactorAuthInfo(
    enabled = enabled
)