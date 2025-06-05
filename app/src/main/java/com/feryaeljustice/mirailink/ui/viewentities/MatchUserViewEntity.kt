package com.feryaeljustice.mirailink.ui.viewentities

import com.feryaeljustice.mirailink.domain.model.User

data class MatchUserViewEntity(
    val id: String,
    val username: String,
    val nickname: String,
    val avatarUrl: String,
    val isBoosted: Boolean = false
)

fun User.toMatchUserViewEntity() = MatchUserViewEntity(
    id = id,
    username = username,
    nickname = nickname,
    avatarUrl = photos.first().url,
    isBoosted = false
)