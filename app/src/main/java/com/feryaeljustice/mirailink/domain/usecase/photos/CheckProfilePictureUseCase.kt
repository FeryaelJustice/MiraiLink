package com.feryaeljustice.mirailink.domain.usecase.photos

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class CheckProfilePictureUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<Boolean> =
        try {
            repository.hasProfilePicture(userId)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error checking profile picture", e)
        }
}
