package com.feryaeljustice.mirailink.ui.mappers

import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.ui.viewentries.MatchUserViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.MinimalUserInfoViewEntry

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