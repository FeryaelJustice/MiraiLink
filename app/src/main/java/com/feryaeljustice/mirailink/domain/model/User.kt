package com.feryaeljustice.mirailink.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String?,
    val phoneNumber: String?,
    val bio: String?,
    val gender: String?,
    val birthdate: String?,
    val photos: List<UserPhoto> = emptyList(),
    val games: List<Game>,
    val animes: List<Anime>,
)

data class MinimalUserInfo(
    val id: String,
    val username: String,
    val email: String?,
    val gender: String?,
    val birthdate: String?,
    val profilePhoto: UserPhoto? = null,
)