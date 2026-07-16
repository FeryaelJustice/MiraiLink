package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.asEmptyResult

class LogoutUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<Unit> =
        repository.logout().asEmptyResult()
}
