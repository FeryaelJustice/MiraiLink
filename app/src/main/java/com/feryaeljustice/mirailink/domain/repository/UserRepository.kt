package com.feryaeljustice.mirailink.domain.repository

import android.net.Uri
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface UserRepository {
    suspend fun autologin(): MiraiLinkResult<String>

    suspend fun login(
        email: String,
        username: String,
        password: String,
    ): MiraiLinkResult<String>

    suspend fun logout(): MiraiLinkResult<Boolean>

    suspend fun register(
        username: String,
        email: String,
        password: String,
        gender: String,
        birthdate: String,
    ): MiraiLinkResult<String>

    suspend fun deleteAccount(): MiraiLinkResult<Unit>

    suspend fun deleteUserPhoto(position: Int): MiraiLinkResult<Unit>

    suspend fun checkIsVerified(): MiraiLinkResult<Boolean>

    suspend fun requestPasswordReset(email: String): MiraiLinkResult<Unit>

    suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String,
    ): MiraiLinkResult<Unit>

    suspend fun requestVerificationCode(
        userId: String,
        type: String,
    ): MiraiLinkResult<Unit>

    suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String,
    ): MiraiLinkResult<Unit>

    suspend fun getCurrentUser(): MiraiLinkResult<User>

    suspend fun getUserById(userId: String): MiraiLinkResult<User>

    suspend fun getUserByUsername(username: String): MiraiLinkResult<User>

    suspend fun updateProfile(
        nickname: String,
        bio: String,
        residenceCountryId: String?,
        residenceRegionId: String?,
        residenceCityId: String?,
        residenceLatitude: Double?,
        residenceLongitude: Double?,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>,
        existingPhotoUrls: List<String?>,
        residenceCountryName: String? = null,
        residenceRegion: String? = null,
        residenceCity: String? = null,
        profession: String? = null,
        religionId: String? = null,
        zodiacSignId: String? = null,
        politicalStanceId: String? = null,
        smokingHabitId: String? = null,
        drinkingHabitId: String? = null,
        sexualOrientationId: String? = null,
        educationLevelId: String? = null,
        relationshipGoalsJson: String? = null,
        familyOptionsJson: String? = null,
        spokenLanguagesJson: String? = null,
        promptsJson: String? = null,
    ): MiraiLinkResult<Unit>

    suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean>

    suspend fun uploadUserPhoto(photo: Uri): MiraiLinkResult<String>

    suspend fun saveUserFCM(fcm: String): MiraiLinkResult<Unit>
}
