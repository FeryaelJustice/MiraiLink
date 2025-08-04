package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.remote.TwoFactorApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import javax.inject.Inject

class TwoFactorRemoteDataSource @Inject constructor(
    private val api: TwoFactorApiService,
) {
    suspend fun get2FAStatus(): MiraiLinkResult<Boolean> {
        return try {
            val twoFactorStatus = api.getTwoFactorStatus().enabled
            MiraiLinkResult.success(twoFactorStatus)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "TwoFactorRemoteDataSource", "get2FAStatus")
        }
    }

    suspend fun setup2FA(): MiraiLinkResult<TwoFactorSetupResponse> {
        return try {
            val setup2FA = api.setupTwoFactor()
            MiraiLinkResult.success(setup2FA)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "TwoFactorRemoteDataSource", "setup2FA")
        }
    }

    suspend fun verify2FA(code: String): MiraiLinkResult<Unit> {
        return try {
            api.verifyTwoFactor(mapOf("token" to code))
            MiraiLinkResult.success(Unit)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "TwoFactorRemoteDataSource", "verify2FA")
        }
    }


    suspend fun disable2FA(codeOrRecoveryCode: String): MiraiLinkResult<Unit> {
        return try {
            api.disableTwoFactor(mapOf("code" to codeOrRecoveryCode))
            MiraiLinkResult.success(Unit)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "TwoFactorRemoteDataSource", "disable2FA")
        }
    }

    suspend fun loginWith2FA(userId: String, code: String): MiraiLinkResult<String> {
        return try {
            val response = api.loginWithTwoFactor(mapOf("userId" to userId, "token" to code))
            MiraiLinkResult.success(response.token)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "TwoFactorRemoteDataSource", "setup2FA")
        }
    }
}