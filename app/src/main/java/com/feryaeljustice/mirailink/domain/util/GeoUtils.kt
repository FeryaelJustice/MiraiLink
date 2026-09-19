package com.feryaeljustice.mirailink.domain.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object GeoUtils {
    private const val EARTH_RADIUS_KM = 6371.0

    // Ubicacion de referencia por defecto: Palma de Mallorca
    const val DEFAULT_FALLBACK_LATITUDE = 39.5696
    const val DEFAULT_FALLBACK_LONGITUDE = 2.6502
    const val DEFAULT_LOCATION_NAME = "Palma de Mallorca"

    /**
     * Calcula la distancia en kilometros entre dos puntos geograficos utilizando
     * la formula de Haversine. Retorna null si alguno de los parametros es nulo.
     */
    fun calculateDistanceKm(
        lat1: Double?,
        lon1: Double?,
        lat2: Double?,
        lon2: Double?,
    ): Double? {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return null
        }

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val originLatRad = Math.toRadians(lat1)
        val destinationLatRad = Math.toRadians(lat2)

        val a =
            sin(dLat / 2) * sin(dLat / 2) +
                cos(originLatRad) * cos(destinationLatRad) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c
    }

    /**
     * Formatea la distancia calculada para mostrar al usuario.
     * Ej: "A 2 km", "A menos de 1 km", "A 45 km".
     */
    fun formatDistance(distanceKm: Double?): String? {
        if (distanceKm == null) return null
        return if (distanceKm < 1.0) {
            "A menos de 1 km"
        } else {
            "A ${distanceKm.toInt()} km"
        }
    }
}
