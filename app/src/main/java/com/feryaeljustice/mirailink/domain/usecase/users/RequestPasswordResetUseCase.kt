package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class RequestPasswordResetUseCase(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(email: String) =
        try {
            repo.requestPasswordReset(email)
        } catch (e: Exception) {
            MiraiLinkResult.Error("RequestPasswordResetUseCase error", e)
        }
}
