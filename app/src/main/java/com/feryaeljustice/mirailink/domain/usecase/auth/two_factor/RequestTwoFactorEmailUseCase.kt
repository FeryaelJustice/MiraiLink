@file:Suppress("ktlint:standard:package-name")
package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class RequestTwoFactorEmailUseCase(private val repo: TwoFactorRepository) {
    suspend operator fun invoke(action: String): MiraiLinkResult<Unit> = repo.requestEmailCode(action)
}
