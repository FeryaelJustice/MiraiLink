package com.feryaeljustice.mirailink.domain.model.settings

import kotlinx.serialization.Serializable

@Serializable
enum class SearchScope {
    RADIUS,
    MY_COUNTRY,
    WORLD,
    SPECIFIC_COUNTRY,
}

@Serializable
data class SearchPreferences(
    val radiusKm: Float = DEFAULT_RADIUS_KM,
    val scope: SearchScope = SearchScope.RADIUS,
    val targetCountryCode: String? = null,
    val matchByLiveLocation: Boolean = false,
    val isPremiumActive: Boolean = false,
) {
    companion object {
        const val MIN_RADIUS_KM = 10f
        const val MAX_RADIUS_KM = 300f
        const val DEFAULT_RADIUS_KM = 40f

        // Preparado para futuro modelo Premium: a partir de 150 km requerirá suscripción
        const val PREMIUM_RADIUS_THRESHOLD_KM = 150f
    }
}
