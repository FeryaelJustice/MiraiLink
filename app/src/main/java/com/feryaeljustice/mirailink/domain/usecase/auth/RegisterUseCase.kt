package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class RegisterUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
    ): MiraiLinkResult<String> =
        repository.register(username, email, password)
}
