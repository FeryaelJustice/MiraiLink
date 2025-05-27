package com.feryaeljustice.mirailink.domain.usecase

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class TestAuthUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<Unit> = repository.testAuth()
}