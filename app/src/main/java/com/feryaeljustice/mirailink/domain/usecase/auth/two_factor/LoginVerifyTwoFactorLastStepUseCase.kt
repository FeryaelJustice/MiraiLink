@file:Suppress("ktlint:standard:package-name")

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class LoginVerifyTwoFactorLastStepUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(
        challengeToken: String,
        code: String,
    ): MiraiLinkResult<LoginResponse> =
        repo.loginVerifyTwoFactorLastStep(challengeToken = challengeToken, code = code)
}
