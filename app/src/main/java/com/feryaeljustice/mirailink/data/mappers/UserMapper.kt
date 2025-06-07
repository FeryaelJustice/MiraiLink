package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.domain.model.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.model.UserPhoto

fun UserDto.toDomain(): User = User(
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
)

fun User.toModel(): UserDto = UserDto(
    id = id,
    username = username,
    nickname = nickname,
    email = email,
    phoneNumber = phoneNumber,
    bio = bio,
    gender = gender,
    birthdate = birthdate,
    animes = animes.map { it.toModel() },
    games = games.map { it.toModel() },
    photos = photos.map { it.toModel() }
)

fun UserPhotoDto.toDomain(): UserPhoto = UserPhoto(
    id = id,
    userId = userId,
    url = url,
    position = position
)

fun UserPhoto.toModel(): UserPhotoDto = UserPhotoDto(
    id = id,
    userId = userId,
    url = url,
    position = position
)

fun UserDto.toMinimalUserInfoDomain(): MinimalUserInfo = MinimalUserInfo(
    id = id,
    username = username,
    nickname = nickname,
    email = email,
    gender = gender,
    birthdate = birthdate,
    profilePhoto = photos.firstOrNull()?.toDomain(),
)

fun UserDto.toMinimalUserInfo(): com.feryaeljustice.mirailink.data.model.response.MinimalUserInfo =
    com.feryaeljustice.mirailink.data.model.response.MinimalUserInfo(
        id = id,
        username = username,
        nickname = nickname,
        avatarUrl = photos.firstOrNull()?.toDomain()?.url,
    )

fun User.toMinimalUserInfo(): MinimalUserInfo = MinimalUserInfo(
    id = id,
    username = username,
    nickname = nickname,
    email = email,
    gender = gender,
    birthdate = birthdate,
    profilePhoto = photos.firstOrNull(),
)