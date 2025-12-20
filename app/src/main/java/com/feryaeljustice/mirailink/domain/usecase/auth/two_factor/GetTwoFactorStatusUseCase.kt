@file:Suppress("ktlint:standard:package-name")

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetTwoFactorStatusUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(userID: String): MiraiLinkResult<Boolean> =
        try {
            repo.get2FAStatus(userID)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting 2FA status", e)
        }
}
