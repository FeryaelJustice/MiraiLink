package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository

class RequestPasswordResetUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(email: String) =
        repo.requestPasswordReset(email)
}
