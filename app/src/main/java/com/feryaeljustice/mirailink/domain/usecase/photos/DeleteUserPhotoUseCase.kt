package com.feryaeljustice.mirailink.domain.usecase.photos

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class DeleteUserPhotoUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(position: Int): MiraiLinkResult<Unit> =
        try {
            repository.deleteUserPhoto(position)
        } catch (e: Exception) {
            MiraiLinkResult.Error("DeleteUserPhotoUseCase error", e)
        }
}
