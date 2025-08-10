package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<Unit> {
        return try {
            repository.logout()
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            MiraiLinkResult.Error("LogoutUseCase error: ", e)
        }
    }
}