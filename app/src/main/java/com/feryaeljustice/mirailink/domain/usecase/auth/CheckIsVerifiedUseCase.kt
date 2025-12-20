package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CheckIsVerifiedUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> =
        try {
            repository.checkIsVerified()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while checking if the user is verified", e)
        }
}
