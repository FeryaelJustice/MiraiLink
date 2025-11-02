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
