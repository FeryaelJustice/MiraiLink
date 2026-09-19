package com.feryaeljustice.mirailink.domain.model.user

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
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
    val fcmToken: String? = "",
    val residenceCity: String? = null,
    val residenceRegion: String? = null,
    val residenceCountryCode: String? = null,
    val residenceLatitude: Double? = null,
    val residenceLongitude: Double? = null,
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null,
    val distanceKm: Double? = null,
    val isTraveler: Boolean = false,
)
