package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String = "",
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
    @SerialName("residence_city")
    val residenceCity: String? = null,
    @SerialName("residence_region")
    val residenceRegion: String? = null,
    @SerialName("residence_country_code")
    val residenceCountryCode: String? = null,
    @SerialName("residence_latitude")
    val residenceLatitude: Double? = null,
    @SerialName("residence_longitude")
    val residenceLongitude: Double? = null,
    @SerialName("current_latitude")
    val currentLatitude: Double? = null,
    @SerialName("current_longitude")
    val currentLongitude: Double? = null,
    @SerialName("distance_km")
    val distanceKm: Double? = null,
    @SerialName("is_traveler")
    val isTraveler: Boolean = false,
    @SerialName("search_radius_km")
    val searchRadiusKm: Int? = null,
    @SerialName("search_scope")
    val searchScope: String? = null,
    @SerialName("search_target_country")
    val searchTargetCountry: String? = null,
    @SerialName("search_match_live_location")
    val searchMatchLiveLocation: Boolean = false,
)
