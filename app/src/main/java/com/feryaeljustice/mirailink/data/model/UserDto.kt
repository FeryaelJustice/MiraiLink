package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("bio")
    val bio: String? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("birthdate")
    val birthdate: String? = null,
    @SerialName("animes")
    val animes: List<AnimeDto> = emptyList(),
    @SerialName("games")
    val games: List<GameDto> = emptyList(),
    @SerialName("photos")
    val photos: List<UserPhotoDto> = emptyList(),
    @SerialName("fcm_token")
    val fcmToken: String = "",
)
