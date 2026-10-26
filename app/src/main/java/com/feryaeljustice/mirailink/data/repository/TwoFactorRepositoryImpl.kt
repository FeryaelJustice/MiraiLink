package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toTwoFactorAuthInfo
import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class TwoFactorRepositoryImpl(
    private val remote: TwoFactorRemoteDataSource,
    private val demoModeManager: com.feryaeljustice.mirailink.data.demo.DemoModeManager? = null,
) : TwoFactorRepository {
    private fun demoError() = MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.DataError.Network.FORBIDDEN)

    override suspend fun get2FAStatus(userID: String): MiraiLinkResult<Boolean> = if (demoModeManager?.isDemoActive() == true) MiraiLinkResult.Success(false) else remote.get2FAStatus(userID)

    override suspend fun setup2FA(): MiraiLinkResult<TwoFactorAuthInfo> =
        when (val result = if (demoModeManager?.isDemoActive() == true) demoError() else remote.setup2FA()) {
            is MiraiLinkResult.Success -> {
                val setup2FA = result.data
                val twoFactorInfo = setup2FA.toTwoFactorAuthInfo()
                MiraiLinkResult.Success(twoFactorInfo)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun verify2FA(code: String, emailCode: String): MiraiLinkResult<Unit> = if (demoModeManager?.isDemoActive() == true) demoError() else remote.verify2FA(code, emailCode)

    override suspend fun requestEmailCode(action: String): MiraiLinkResult<Unit> = if (demoModeManager?.isDemoActive() == true) demoError() else remote.requestEmailCode(action)

    override suspend fun disable2FA(codeOrRecoveryCode: String): MiraiLinkResult<Unit> =
        if (demoModeManager?.isDemoActive() == true) demoError() else remote.disable2FA(codeOrRecoveryCode)

    override suspend fun loginVerifyTwoFactorLastStep(
        challengeToken: String,
        code: String,
    ): MiraiLinkResult<LoginResponse> = remote.loginVerifyTwoFactorLastStep(challengeToken, code)
}
