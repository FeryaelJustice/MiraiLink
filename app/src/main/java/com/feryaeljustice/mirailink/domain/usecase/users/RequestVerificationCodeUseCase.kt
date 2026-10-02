package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository

class RequestVerificationCodeUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(
        userId: String,
        type: String,
    ) = repo.requestVerificationCode(userId, type)
}
