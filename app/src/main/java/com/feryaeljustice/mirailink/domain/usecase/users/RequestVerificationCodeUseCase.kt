package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class RequestVerificationCodeUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(
        userId: String,
        type: String,
    ) = repo.requestVerificationCode(userId, type)
}
