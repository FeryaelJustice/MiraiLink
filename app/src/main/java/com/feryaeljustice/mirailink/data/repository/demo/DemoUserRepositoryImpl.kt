package com.feryaeljustice.mirailink.data.repository.demo

import android.net.Uri
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.toDomainUser
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.serialization.json.Json

class DemoUserRepositoryImpl(
    private val database: MiraiLinkDemoDatabase,
    private val seeder: DemoDataSeeder,
    private val json: Json = Json { ignoreUnknownKeys = true },
) : UserRepository {

    override suspend fun autologin(): MiraiLinkResult<String> {
        seeder.seedInitialDataIfEmpty()
        return MiraiLinkResult.Success(DemoDataSeeder.DEMO_USER_ID)
    }

    override suspend fun login(
        email: String,
        username: String,
        password: String,
    ): MiraiLinkResult<String> {
        seeder.seedInitialDataIfEmpty()
        return MiraiLinkResult.Success(DemoDataSeeder.DEMO_USER_ID)
    }

    override suspend fun logout(): MiraiLinkResult<Boolean> {
        return MiraiLinkResult.Success(true)
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        gender: String,
        birthdate: String,
    ): MiraiLinkResult<String> {
        seeder.seedInitialDataIfEmpty()
        return MiraiLinkResult.Success(DemoDataSeeder.DEMO_USER_ID)
    }

    override suspend fun deleteAccount(): MiraiLinkResult<Unit> {
        seeder.resetDemoData()
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun deleteUserPhoto(position: Int): MiraiLinkResult<Unit> {
        val profile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
            ?: return MiraiLinkResult.Success(Unit)

        val photos: MutableList<UserPhoto> = runCatching {
            json.decodeFromString<List<UserPhoto>>(profile.photosJson).toMutableList()
        }.getOrDefault(mutableListOf())

        photos.removeAll { it.position == position }
        val updatedProfile = profile.copy(photosJson = json.encodeToString(photos))
        database.userDao().updateUserProfile(updatedProfile)

        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun checkIsVerified(): MiraiLinkResult<Boolean> {
        return MiraiLinkResult.Success(true)
    }

    override suspend fun requestPasswordReset(email: String): MiraiLinkResult<Unit> {
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String,
    ): MiraiLinkResult<Unit> {
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun requestVerificationCode(
        userId: String,
        type: String,
    ): MiraiLinkResult<Unit> {
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String,
    ): MiraiLinkResult<Unit> {
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): MiraiLinkResult<User> {
        seeder.seedInitialDataIfEmpty()
        val profile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        return if (profile != null) {
            MiraiLinkResult.Success(profile.toDomainUser())
        } else {
            seeder.resetDemoData()
            val fallback = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
            MiraiLinkResult.Success(fallback!!.toDomainUser())
        }
    }

    override suspend fun getUserById(userId: String): MiraiLinkResult<User> {
        if (userId == DemoDataSeeder.DEMO_USER_ID) {
            return getCurrentUser()
        }

        val feedUser = database.userDao().getFeedUserById(userId)
        return if (feedUser != null) {
            MiraiLinkResult.Success(feedUser.toDomainUser())
        } else {
            getCurrentUser()
        }
    }

    override suspend fun getUserByUsername(username: String): MiraiLinkResult<User> {
        seeder.seedInitialDataIfEmpty()
        val allUsers = database.userDao().getAllFeedUsers()
        val found = allUsers.find { it.username.equals(username, ignoreCase = true) }
        if (found != null) {
            return MiraiLinkResult.Success(found.toDomainUser())
        }
        val myProfile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        if (myProfile != null && (myProfile.username.equals(username, ignoreCase = true) || username.equals("me", ignoreCase = true))) {
            return MiraiLinkResult.Success(myProfile.toDomainUser())
        }
        return MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.DataError.Network.NOT_FOUND)
    }

    override suspend fun updateProfile(
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
        residenceCountryName: String?,
        residenceRegion: String?,
        residenceCity: String?,
        profession: String?,
        religionId: String?,
        zodiacSignId: String?,
        politicalStanceId: String?,
        smokingHabitId: String?,
        drinkingHabitId: String?,
        sexualOrientationId: String?,
        educationLevelId: String?,
        relationshipGoalsJson: String?,
        familyOptionsJson: String?,
        spokenLanguagesJson: String?,
        promptsJson: String?,
    ): MiraiLinkResult<Unit> {
        val currentProfile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
            ?: return MiraiLinkResult.Success(Unit)

        val finalPhotos = mutableListOf<UserPhoto>()
        for (index in 0 until 4) {
            val position = index + 1
            val uri = photoUris.getOrNull(index)
            val existingUrl = existingPhotoUrls.getOrNull(index)
            if (uri != null) {
                finalPhotos.add(UserPhoto(DemoDataSeeder.DEMO_USER_ID, uri.toString(), position))
            } else if (existingUrl != null) {
                finalPhotos.add(UserPhoto(DemoDataSeeder.DEMO_USER_ID, existingUrl, position))
            }
        }
        val compactedPhotos = finalPhotos.mapIndexed { idx, photo ->
            photo.copy(position = idx + 1)
        }

        val updatedProfile = currentProfile.copy(
            nickname = nickname,
            bio = bio,
            residenceCountryCode = currentProfile.residenceCountryCode,
            residenceRegion = currentProfile.residenceRegion,
            residenceCity = currentProfile.residenceCity,
            residenceLatitude = residenceLatitude ?: currentProfile.residenceLatitude,
            residenceLongitude = residenceLongitude ?: currentProfile.residenceLongitude,
            animesJson = animesJson,
            gamesJson = gamesJson,
            photosJson = json.encodeToString(compactedPhotos),
        )

        database.userDao().updateUserProfile(updatedProfile)
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean> {
        return MiraiLinkResult.Success(true)
    }

    override suspend fun uploadUserPhoto(photo: Uri): MiraiLinkResult<String> {
        return MiraiLinkResult.Success(photo.toString())
    }

    override suspend fun saveUserFCM(fcm: String): MiraiLinkResult<Unit> {
        return MiraiLinkResult.Success(Unit)
    }
}
