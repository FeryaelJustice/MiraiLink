package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.remote.TwoFactorApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.data.util.safeApiUnitResponse
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class TwoFactorRemoteDataSource(
    private val api: TwoFactorApiService,
) {
    suspend fun get2FAStatus(userID: String): MiraiLinkResult<Boolean> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getTwoFactorStatus(mapOf("userId" to userID)).enabled
        }

    suspend fun requestEmailCode(action: String): MiraiLinkResult<Unit> =
        safeApiUnitResponse(NetworkOperation.TWO_FACTOR) {
            api.requestTwoFactorEmail(mapOf("action" to action))
        }

    suspend fun setup2FA(): MiraiLinkResult<TwoFactorSetupResponse> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.setupTwoFactor()
        }

    suspend fun verify2FA(code: String, emailCode: String): MiraiLinkResult<Unit> =
        safeApiUnitResponse(NetworkOperation.TWO_FACTOR) {
            api.verifyTwoFactor(mapOf("token" to code, "emailCode" to emailCode))
        }

    suspend fun disable2FA(codeOrRecoveryCode: String = ""): MiraiLinkResult<Unit> =
        safeApiUnitResponse(NetworkOperation.TWO_FACTOR) {
            api.disableTwoFactor(mapOf("emailCode" to codeOrRecoveryCode))
        }

    suspend fun loginVerifyTwoFactorLastStep(
        challengeToken: String,
        code: String,
    ): MiraiLinkResult<LoginResponse> =
        safeApiCall(NetworkOperation.TWO_FACTOR) {
            api.loginVerifyTwoFactorLastStep(
                mapOf("challengeToken" to challengeToken, "code" to code),
            )
        }
}
