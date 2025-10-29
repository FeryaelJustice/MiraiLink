package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ConfirmVerificationCodeUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(userId: String, token: String, type: String) =
        try {
            repo.confirmVerificationCode(userId, token, type)
        } catch (e: Exception) {
            MiraiLinkResult.Error("ConfirmVerificationCodeUseCase error", e)
        }
}