package com.feryaeljustice.mirailink.ui.mappers

import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.ui.viewentries.user.MatchUserViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

fun User.toUserViewEntry(): UserViewEntry = UserViewEntry(
    id = id,
    username = username,
    nickname = nickname,
    email = email,
    phoneNumber = phoneNumber,
    bio = bio,
    gender = gender,
    birthdate = birthdate,
    photos = photos.map { it.toUserPhotoViewEntry() },
    games = games.map { it.toGameViewEntry() },
    animes = animes.map { it.toAnimeViewEntry() }
)

fun User.toMatchUserViewEntry() = MatchUserViewEntry(
    id = id,
    username = username,
    nickname = nickname,
    avatarUrl = photos.firstOrNull()?.url ?: TEMPORAL_PLACEHOLDER_PICTURE_URL,
    isBoosted = false
)

fun MinimalUserInfo.toMinimalUserInfoViewEntry(): MinimalUserInfoViewEntry =
    MinimalUserInfoViewEntry(
        id = id,
        username = username,
        nickname = nickname,
        email = email,
        gender = gender,
        birthdate = birthdate,
        profilePhoto = profilePhoto
    )