package com.feryaeljustice.mirailink.data.model.request.location

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationPingRequest(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("city") val city: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
)
