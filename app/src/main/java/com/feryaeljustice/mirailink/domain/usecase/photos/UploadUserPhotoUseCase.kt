package com.feryaeljustice.mirailink.domain.usecase.photos

import android.net.Uri
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class UploadUserPhotoUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(photo: Uri): MiraiLinkResult<String> {
        return repository.uploadUserPhoto(photo)
    }
}