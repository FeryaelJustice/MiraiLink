package com.feryaeljustice.mirailink.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GeoUtilsTest {

    @Test
    fun calculateDistanceKm_sameCoordinates_returnsZero() {
        val distance = GeoUtils.calculateDistanceKm(
            lat1 = 39.5696,
            lon1 = 2.6502,
            lat2 = 39.5696,
            lon2 = 2.6502,
        )

        assertNotNull(distance)
        assertEquals(0.0, distance!!, 0.001)
    }

    @Test
    fun calculateDistanceKm_palmaToInca_isApproximatelyThirtyKm() {
        val distance = GeoUtils.calculateDistanceKm(
            lat1 = 39.5696,
            lon1 = 2.6502,
            lat2 = 39.7210,
            lon2 = 2.9110,
        )

        assertNotNull(distance)
        // Palma a Inca son aprox 28-30 km en linea recta (~27.9 km)
        assertTrue("Distance should be between 25 and 35 km, but was $distance", distance!! in 25.0..35.0)
    }

    @Test
    fun calculateDistanceKm_palmaToBarcelona_isApproximatelyTwoHundredKm() {
        val distance = GeoUtils.calculateDistanceKm(
            lat1 = 39.5696,
            lon1 = 2.6502,
            lat2 = 41.3879,
            lon2 = 2.1699,
        )

        assertNotNull(distance)
        // Palma a Barcelona son aprox 205-210 km
        assertTrue("Distance should be between 200 and 220 km, but was $distance", distance!! in 200.0..220.0)
    }

    @Test
    fun calculateDistanceKm_nullCoordinates_returnsNull() {
        assertNull(GeoUtils.calculateDistanceKm(null, 2.6502, 39.5696, 2.6502))
        assertNull(GeoUtils.calculateDistanceKm(39.5696, null, 39.5696, 2.6502))
        assertNull(GeoUtils.calculateDistanceKm(39.5696, 2.6502, null, 2.6502))
        assertNull(GeoUtils.calculateDistanceKm(39.5696, 2.6502, 39.5696, null))
    }

    @Test
    fun formatDistance_formatsCorrectly() {
        assertEquals("A menos de 1 km", GeoUtils.formatDistance(0.4))
        assertEquals("A 2 km", GeoUtils.formatDistance(2.1))
        assertEquals("A 40 km", GeoUtils.formatDistance(40.8))
        assertNull(GeoUtils.formatDistance(null))
    }
}
