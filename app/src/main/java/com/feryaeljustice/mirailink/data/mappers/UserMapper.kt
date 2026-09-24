package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto

fun UserDto.toDomain(): User =
    User(
        id = id,
        username = username,
        nickname = nickname,
        email = email,
        phoneNumber = phoneNumber,
        bio = bio,
        gender = gender,
        birthdate = birthdate,
        photos = photos.map { it.toDomain() },
        games = games.map { it.toDomain() },
        animes = animes.map { it.toDomain() },
        fcmToken = fcmToken,
        residenceCountryId = residenceCountryId,
        residenceRegionId = residenceRegionId,
        residenceCityId = residenceCityId,
        residenceCity = residenceCity,
        residenceRegion = residenceRegion,
        residenceCountryCode = residenceCountryCode,
        residenceCountry = residenceCountry,
        residenceLatitude = residenceLatitude,
        residenceLongitude = residenceLongitude,
        currentLatitude = currentLatitude,
        currentLongitude = currentLongitude,
        distanceKm = distanceKm,
        isTraveler = isTraveler,
        profession = profession,
        religionId = religionId,
        religion = religion,
        zodiacSignId = zodiacSignId,
        zodiacSign = zodiacSign,
        politicalStanceId = politicalStanceId,
        politicalStance = politicalStance,
        smokingHabitId = smokingHabitId,
        smokingHabit = smokingHabit,
        drinkingHabitId = drinkingHabitId,
        drinkingHabit = drinkingHabit,
        sexualOrientationId = sexualOrientationId,
        sexualOrientation = sexualOrientation,
        educationLevelId = educationLevelId,
        educationLevel = educationLevel,
        relationshipGoalIds = relationshipGoals.map { it.id },
        relationshipGoals = relationshipGoals.map { it.label ?: it.code },
        familyOptionIds = familyOptions.map { it.id },
        familyOptions = familyOptions.map { it.label ?: it.code },
        spokenLanguageIds = spokenLanguages.map { it.id },
        spokenLanguages = spokenLanguages.map { it.label ?: it.code },
        prompts = prompts.map {
            com.feryaeljustice.mirailink.domain.model.user.GamerPromptAnswer(
                promptId = it.promptId,
                question = it.question.orEmpty(),
                answer = it.answer,
            )
        },
    )

fun MinimalUserInfoResponse.toMinimalUserInfo(): MinimalUserInfo =
    MinimalUserInfo(
        id = id,
        username = username,
        nickname = nickname,
        profilePhoto = UserPhoto(userId = id, url = avatarUrl.orEmpty(), position = 1),
    )

fun UserDto.toMinimalUserInfo(): MinimalUserInfo =
    MinimalUserInfo(
        id = id,
        username = username,
        nickname = nickname,
        email = email.orEmpty(),
        gender = gender.orEmpty(),
        birthdate = birthdate.orEmpty(),
        profilePhoto = photos.firstOrNull()?.toDomain(),
    )

fun User.toMinimalUserInfo(): MinimalUserInfo =
    MinimalUserInfo(
        id = id,
        username = username,
        nickname = nickname,
        email = email.orEmpty(),
        gender = gender.orEmpty(),
        birthdate = birthdate.orEmpty(),
        profilePhoto = photos.firstOrNull(),
    )
