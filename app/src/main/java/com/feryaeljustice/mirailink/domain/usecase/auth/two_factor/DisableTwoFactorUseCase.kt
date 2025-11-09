/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DisableTwoFactorUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(codeOrRecoveryCode: String): MiraiLinkResult<Unit> =
        try {
            repo.disable2FA(codeOrRecoveryCode)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while disabling 2FA", e)
        }
}
