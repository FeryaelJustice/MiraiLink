package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository

class ConfirmPasswordResetUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(
        email: String,
        token: String,
        newPassword: String,
    ) = repo.confirmPasswordReset(email, token, newPassword)
}
