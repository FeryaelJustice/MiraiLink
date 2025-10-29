/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetTwoFactorStatusUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke(userID: String): MiraiLinkResult<Boolean> {
        return try {
            repo.get2FAStatus(userID)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting 2FA status", e)
        }
    }
}