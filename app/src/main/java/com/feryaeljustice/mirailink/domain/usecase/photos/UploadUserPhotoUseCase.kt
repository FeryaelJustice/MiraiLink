package com.feryaeljustice.mirailink.domain.usecase.photos

import android.net.Uri
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class UploadUserPhotoUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(photo: Uri): MiraiLinkResult<String> {
        return try {
            repository.uploadUserPhoto(photo)
        } catch (e: Exception) {
            MiraiLinkResult.Error("UploadUserPhotoUseCase error", e)
        }
    }
}