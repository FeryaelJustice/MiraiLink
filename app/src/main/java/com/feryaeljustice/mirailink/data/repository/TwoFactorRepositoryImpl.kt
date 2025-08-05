package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource
import com.feryaeljustice.mirailink.domain.mappers.toTwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class TwoFactorRepositoryImpl @Inject constructor(
    private val remote: TwoFactorRemoteDataSource,
) : TwoFactorRepository {
    override suspend fun get2FAStatus(): MiraiLinkResult<Boolean> = remote.get2FAStatus()
    override suspend fun setup2FA(): MiraiLinkResult<TwoFactorAuthInfo> {
        return when (val result = remote.setup2FA()) {
            is MiraiLinkResult.Success -> {
                val setup2FA = result.data
                val twoFactorInfo = setup2FA.toTwoFactorAuthInfo()
                MiraiLinkResult.Success(twoFactorInfo)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun verify2FA(code: String): MiraiLinkResult<Unit> = remote.verify2FA(code)

    override suspend fun disable2FA(codeOrRecoveryCode: String): MiraiLinkResult<Unit> =
        remote.disable2FA(codeOrRecoveryCode)

    override suspend fun loginVerifyTwoFactorLastStep(
        userId: String,
        code: String
    ): MiraiLinkResult<String> =
        remote.loginVerifyTwoFactorLastStep(userId, code)
}