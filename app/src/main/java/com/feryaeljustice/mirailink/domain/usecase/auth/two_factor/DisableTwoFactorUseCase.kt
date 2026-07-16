@file:Suppress("ktlint:standard:package-name")

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DisableTwoFactorUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(codeOrRecoveryCode: String): MiraiLinkResult<Unit> =
        repo.disable2FA(codeOrRecoveryCode)
}
