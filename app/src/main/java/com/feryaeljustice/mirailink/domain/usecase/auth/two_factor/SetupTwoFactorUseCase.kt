/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class SetupTwoFactorUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke(): MiraiLinkResult<TwoFactorAuthInfo> {
        return try {
            repo.setup2FA()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while setting up 2FA", e)
        }
    }
}