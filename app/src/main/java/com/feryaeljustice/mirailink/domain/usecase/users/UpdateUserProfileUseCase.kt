package com.feryaeljustice.mirailink.domain.usecase.users

import android.net.Uri
import android.util.Log
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        nickname: String,
        bio: String,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>
    ): MiraiLinkResult<Unit> {
        Log.d(
            "UpdateUserProfileUseCase",
            "invoke: $nickname $bio $animesJson $gamesJson $photoUris"
        )
        return repository.updateProfile(
            nickname = nickname,
            bio = bio,
            animesJson = animesJson,
            gamesJson = gamesJson,
            photoUris = photoUris
        )
    }
}
