package com.feryaeljustice.mirailink.data.model.request.settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSearchSettingsRequest(
    @SerialName("search_radius_km") val searchRadiusKm: Int,
    @SerialName("search_scope") val searchScope: String,
    @SerialName("search_target_country") val searchTargetCountry: String? = null,
    @SerialName("search_match_live_location") val searchMatchLiveLocation: Boolean = false,
)
