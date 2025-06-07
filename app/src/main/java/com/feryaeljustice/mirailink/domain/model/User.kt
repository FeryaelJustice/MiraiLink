package com.feryaeljustice.mirailink.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String?,
    val phoneNumber: String?,
    val bio: String?,
    val gender: String?,
    val birthdate: String?,
    val photos: List<UserPhoto> = emptyList(),
    val games: List<Game>,
    val animes: List<Anime>,
)

@Serializable
data class MinimalUserInfo(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String?,
    val gender: String?,
    val birthdate: String?,
    val profilePhoto: UserPhoto? = null,
)