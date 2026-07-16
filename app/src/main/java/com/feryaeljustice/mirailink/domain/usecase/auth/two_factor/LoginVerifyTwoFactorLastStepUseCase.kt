@file:Suppress("ktlint:standard:package-name")

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class LoginVerifyTwoFactorLastStepUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(
        userId: String,
        code: String,
    ): MiraiLinkResult<Unit> =
        repo.loginVerifyTwoFactorLastStep(userId = userId, code = code)
}
