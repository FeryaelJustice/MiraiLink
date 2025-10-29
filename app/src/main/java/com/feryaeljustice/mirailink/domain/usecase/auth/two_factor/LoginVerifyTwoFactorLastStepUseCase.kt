/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class LoginVerifyTwoFactorLastStepUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke(userId: String, code: String): MiraiLinkResult<String> {
        return try {
            repo.loginVerifyTwoFactorLastStep(userId = userId, code = code)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred during 2FA login verification", e)
        }
    }
}