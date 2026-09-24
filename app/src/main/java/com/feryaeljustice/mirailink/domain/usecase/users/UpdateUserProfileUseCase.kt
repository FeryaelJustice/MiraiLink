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
    ): MiraiLinkResult<Unit> =
        repository.updateProfile(
            nickname = nickname,
            bio = bio,
            residenceCountryId = residenceCountryId,
            residenceRegionId = residenceRegionId,
            residenceCityId = residenceCityId,
            residenceCountryName = residenceCountryName,
            residenceRegion = residenceRegion,
            residenceCity = residenceCity,
            residenceLatitude = residenceLatitude,
            residenceLongitude = residenceLongitude,
            animesJson = animesJson,
            gamesJson = gamesJson,
            photoUris = photoUris,
            existingPhotoUrls = existingPhotoUrls,
            profession = profession,
            religionId = religionId,
            zodiacSignId = zodiacSignId,
            politicalStanceId = politicalStanceId,
            smokingHabitId = smokingHabitId,
            drinkingHabitId = drinkingHabitId,
            sexualOrientationId = sexualOrientationId,
            educationLevelId = educationLevelId,
            relationshipGoalsJson = relationshipGoalsJson,
            familyOptionsJson = familyOptionsJson,
            spokenLanguagesJson = spokenLanguagesJson,
            promptsJson = promptsJson,
        )
}
