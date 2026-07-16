package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DeleteAccountUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<Unit> =
        repository.deleteAccount()
}
