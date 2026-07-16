@file:Suppress("ktlint:standard:package-name")

package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetTwoFactorStatusUseCase(
    private val repo: TwoFactorRepository,
) {
    suspend operator fun invoke(userID: String): MiraiLinkResult<Boolean> =
        repo.get2FAStatus(userID)
}
