package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CheckProfilePictureUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<Boolean> =
        repository.hasProfilePicture(userId)
}