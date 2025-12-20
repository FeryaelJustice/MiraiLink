@file:Suppress("ktlint:standard:package-name")

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class VerifyTwoFactorUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(code: String): MiraiLinkResult<Unit> =
        try {
            repo.verify2FA(code = code)
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while verifying 2FA", e)
        }
}
