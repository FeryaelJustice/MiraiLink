package com.feryaeljustice.mirailink.domain.usecase.photos

import android.net.Uri
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class UploadUserPhotoUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(photo: Uri): MiraiLinkResult<String> =
        try {
            repository.uploadUserPhoto(photo)
        } catch (e: Exception) {
            MiraiLinkResult.Error("UploadUserPhotoUseCase error", e)
        }
}
