package com.feryaeljustice.mirailink.domain.usecase.photos

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class DeleteUserPhotoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(position: Int): MiraiLinkResult<Unit> {
        return repository.deleteUserPhoto(position)
    }
}