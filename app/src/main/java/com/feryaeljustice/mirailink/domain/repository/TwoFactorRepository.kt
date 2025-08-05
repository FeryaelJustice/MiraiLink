package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface TwoFactorRepository {
    suspend fun get2FAStatus(): MiraiLinkResult<Boolean>
    suspend fun setup2FA(): MiraiLinkResult<TwoFactorAuthInfo>
    suspend fun verify2FA(code: String): MiraiLinkResult<Unit>
    suspend fun disable2FA(codeOrRecoveryCode: String): MiraiLinkResult<Unit>
    suspend fun loginVerifyTwoFactorLastStep(userId: String, code: String): MiraiLinkResult<String>
}