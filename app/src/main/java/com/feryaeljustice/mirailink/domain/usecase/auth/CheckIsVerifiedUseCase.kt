package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CheckIsVerifiedUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> =
        repository.checkIsVerified()
}
