package com.feryaeljustice.mirailink.domain.repository

import android.net.Uri
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface UserRepository {
    suspend fun autologin(): MiraiLinkResult<String>
    suspend fun login(email: String, username: String, password: String): MiraiLinkResult<String>
    suspend fun logout(): MiraiLinkResult<Boolean>
    suspend fun register(username: String, email: String, password: String): MiraiLinkResult<String>
    suspend fun checkIsVerified(): MiraiLinkResult<Boolean>
    suspend fun requestPasswordReset(email: String): MiraiLinkResult<String>
    suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String
    ): MiraiLinkResult<String>

    suspend fun requestVerificationCode(userId: String, type: String): MiraiLinkResult<String>
    suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String
    ): MiraiLinkResult<String>

    suspend fun getCurrentUser(): MiraiLinkResult<User>
    suspend fun getUserById(userId: String): MiraiLinkResult<User>
    suspend fun updateProfile(
        nickname: String,
        bio: String,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>
    ): MiraiLinkResult<Unit>
    suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean>
    suspend fun uploadUserPhoto(photo: Uri): MiraiLinkResult<String>
}