package com.feryaeljustice.mirailink.domain.usecase.photos

import android.util.Log
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CheckProfilePictureUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<Boolean> {
        return try {
            Log.d("CheckProfilePictureUseCase", "Checking profile picture for user: $userId")
            repository.hasProfilePicture(userId)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error checking profile picture", e)
        }
    }
}