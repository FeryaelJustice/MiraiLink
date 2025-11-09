package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class LoginUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
    ): MiraiLinkResult<String> =
        try {
            repository.login(email, username, password)
        } catch (e: Exception) {
            MiraiLinkResult.Error("LoginUseCase error: ", e)
        }
}
