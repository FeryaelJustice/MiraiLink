package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class RequestVerificationCodeUseCase
    @Inject
    constructor(
        private val repo: UserRepository,
    ) {
        suspend operator fun invoke(
            userId: String,
            type: String,
        ) = try {
            repo.requestVerificationCode(userId, type)
        } catch (e: Exception) {
            MiraiLinkResult.Error("RequestVerificationCodeUseCase error:", e)
    }
}
