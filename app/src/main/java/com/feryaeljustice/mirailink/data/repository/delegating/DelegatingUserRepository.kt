package com.feryaeljustice.mirailink.data.repository.delegating

import android.net.Uri
import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DelegatingUserRepository(
    private val remoteRepo: UserRepository,
    private val demoRepo: UserRepository,
    private val demoModeManager: DemoModeManager,
) : UserRepository {

    private fun targetRepo(): UserRepository =
        if (demoModeManager.isDemoActive()) demoRepo else remoteRepo

    override suspend fun autologin(): MiraiLinkResult<String> = targetRepo().autologin()

    override suspend fun login(
        email: String,
        username: String,
        password: String,
    ): MiraiLinkResult<String> = targetRepo().login(email, username, password)

    override suspend fun logout(): MiraiLinkResult<Boolean> = targetRepo().logout()

    override suspend fun register(
        username: String,
        email: String,
        password: String,
    ): MiraiLinkResult<String> = targetRepo().register(username, email, password)

    override suspend fun deleteAccount(): MiraiLinkResult<Unit> = targetRepo().deleteAccount()

    override suspend fun deleteUserPhoto(position: Int): MiraiLinkResult<Unit> =
        targetRepo().deleteUserPhoto(position)

    override suspend fun checkIsVerified(): MiraiLinkResult<Boolean> =
        targetRepo().checkIsVerified()

    override suspend fun requestPasswordReset(email: String): MiraiLinkResult<Unit> =
        targetRepo().requestPasswordReset(email)

    override suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String,
    ): MiraiLinkResult<Unit> = targetRepo().confirmPasswordReset(email, token, newPassword)

    override suspend fun requestVerificationCode(
        userId: String,
        type: String,
    ): MiraiLinkResult<Unit> = targetRepo().requestVerificationCode(userId, type)

    override suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String,
    ): MiraiLinkResult<Unit> = targetRepo().confirmVerificationCode(userId, token, type)

    override suspend fun getCurrentUser(): MiraiLinkResult<User> = targetRepo().getCurrentUser()

    override suspend fun getUserById(userId: String): MiraiLinkResult<User> =
        targetRepo().getUserById(userId)

    override suspend fun updateProfile(
        nickname: String,
        bio: String,
        gender: String?,
        birthdate: String?,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>,
        existingPhotoUrls: List<String?>,
    ): MiraiLinkResult<Unit> = targetRepo().updateProfile(
        nickname,
        bio,
        gender,
        birthdate,
        animesJson,
        gamesJson,
        photoUris,
        existingPhotoUrls,
    )

    override suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean> =
        targetRepo().hasProfilePicture(userId)

    override suspend fun uploadUserPhoto(photo: Uri): MiraiLinkResult<String> =
        targetRepo().uploadUserPhoto(photo)

    override suspend fun saveUserFCM(fcm: String): MiraiLinkResult<Unit> =
        targetRepo().saveUserFCM(fcm)
}
