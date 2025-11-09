package com.feryaeljustice.mirailink.domain.usecase.users

import android.net.Uri
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class UpdateUserProfileUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        bio: String,
        gender: String?,
        birthdate: String?,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>,
        existingPhotoUrls: List<String?>,
    ): MiraiLinkResult<Unit> =
        repository.updateProfile(
            nickname = nickname,
            bio = bio,
            gender = gender,
            birthdate = birthdate,
            animesJson = animesJson,
            gamesJson = gamesJson,
            photoUris = photoUris,
            existingPhotoUrls = existingPhotoUrls,
        )
}
