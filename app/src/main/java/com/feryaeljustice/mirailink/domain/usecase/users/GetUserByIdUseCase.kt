package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetUserByIdUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<User> = repository.getUserById(userId)
}
