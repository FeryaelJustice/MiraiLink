package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository

class ConfirmVerificationCodeUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(
        userId: String,
        token: String,
        type: String,
    ) = repo.confirmVerificationCode(userId, token, type)
}
