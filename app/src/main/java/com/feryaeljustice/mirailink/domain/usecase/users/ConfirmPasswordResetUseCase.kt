package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ConfirmPasswordResetUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(
        email: String,
        token: String,
        newPassword: String,
    ) = try {
        repo.confirmPasswordReset(email, token, newPassword)
    } catch (e: Exception) {
        MiraiLinkResult.Error("ConfirmPasswordResetUseCase error", e)
    }
}
