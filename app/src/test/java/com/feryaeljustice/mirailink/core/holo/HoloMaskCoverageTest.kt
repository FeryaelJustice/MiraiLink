package com.feryaeljustice.mirailink.core.holo

import org.junit.Assert.*
import org.junit.Test

/** Recortes defectuosos nunca habilitan separacion de capas. */
class HoloMaskCoverageTest {
    @Test fun `mascaras vacias completas e invalidas usan alternativa`() {
        listOf(FloatArray(100), FloatArray(100) { 1f }, FloatArray(100) { Float.NaN })
            .forEach { assertFalse(HoloMaskCoverage.supportsDepth(HoloMask(10, 10, it), 1f, 1f)) }
    }

    @Test fun `movimiento que descubre silueta se rechaza`() {
        val mask = square()
        assertFalse(HoloMaskCoverage.supportsDepth(mask, 5f, 5f))
    }

    @Test fun `silueta cubierta con profundidad pequena se acepta`() {
        assertTrue(HoloMaskCoverage.supportsDepth(square(), 0.1f, 0.1f))
    }

    @Test fun `interpolacion y bordes suavizados conservan rango`() {
        val mask = HoloMask(2, 2, floatArrayOf(0f, 1f, 0f, 1f))
        assertEquals(0.5f, mask.sample(0.5f, 0.5f), 0.001f)
        assertEquals(0f, mask.sample(-1f, 0f), 0f)
        assertEquals(0.5f, mask.alpha(0.5f), 0.001f)
    }

    private fun square() = HoloMask(100, 100, FloatArray(10_000) {
        if (it % 100 in 25..74 && it / 100 in 25..74) 1f else 0f
    })
}
