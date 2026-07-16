package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ConfirmVerificationCodeUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(
        userId: String,
        token: String,
        type: String,
    ) = repo.confirmVerificationCode(userId, token, type)
}
