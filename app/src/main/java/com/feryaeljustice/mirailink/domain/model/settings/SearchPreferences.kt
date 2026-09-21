package com.feryaeljustice.mirailink.domain.model.settings

import kotlinx.serialization.Serializable

@Serializable
enum class SearchScope(val wireValue: String) {
    RADIUS_RESIDENCE("radius_residence"),
    RADIUS_ACTIVE("radius_active"),
    MY_COUNTRY("country"),
    WORLD("world"),
    SPECIFIC_COUNTRY("specific_country"),
    ;

    companion object {
        fun fromWireValue(
            value: String?,
            legacyMatchByLiveLocation: Boolean = false,
        ): SearchScope =
            when (value?.trim()?.lowercase()) {
                "radius_residence" -> RADIUS_RESIDENCE
                "radius_active" -> RADIUS_ACTIVE
                "radius" -> if (legacyMatchByLiveLocation) RADIUS_ACTIVE else RADIUS_RESIDENCE
                "country", "my_country" -> MY_COUNTRY
                "world" -> WORLD
                "specific_country" -> SPECIFIC_COUNTRY
                else -> RADIUS_RESIDENCE
            }
    }

    fun isRadiusScope(): Boolean = this == RADIUS_RESIDENCE || this == RADIUS_ACTIVE
}

@Serializable
data class SearchPreferences(
    val radiusKm: Float = DEFAULT_RADIUS_KM,
    val scope: SearchScope = SearchScope.RADIUS_RESIDENCE,
    val targetCountryCode: String? = null,
    val isPremiumActive: Boolean = false,
) {
    companion object {
        const val MIN_RADIUS_KM = 10f
        const val MAX_RADIUS_KM = 800f
        const val DEFAULT_RADIUS_KM = 40f

        // Preparado para futuro modelo Premium: a partir de 250 km requerirá suscripción
        const val PREMIUM_RADIUS_THRESHOLD_KM = 250f
    }
}
