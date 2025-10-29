/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class VerifyTwoFactorUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke(code: String): MiraiLinkResult<Unit> {
        return try {
            repo.verify2FA(code = code)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while verifying 2FA", e)
        }
    }
}