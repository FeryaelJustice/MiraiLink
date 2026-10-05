package com.feryaeljustice.mirailink.domain.model.holo

import org.junit.Assert.*
import org.junit.Test

/** Politica de recursos y limites de movimiento, sin hardware. */
class HoloMotionTest {
    @Test fun `cada condicion inactiva detiene efecto`() {
        val active = HoloActivation(true, true, true, true, true)
        assertTrue(active.active)
        listOf(active.copy(enabled = false), active.copy(resumed = false),
            active.copy(focused = false), active.copy(deviceAllowsMotion = false),
            active.copy(sensorAvailable = false), active.copy(interacting = true),
            active.copy(covered = true)).forEach { assertFalse(it.active) }
    }

    @Test fun `primera postura calibra y movimientos permanecen limitados`() {
        val filter = HoloTiltFilter()
        assertEquals(HoloTilt(), filter.update(0.7f, -0.4f))
        repeat(100) { filter.update(2f, 2f) }
        val result = filter.update(2f, 2f)
        assertTrue(result.x in 0f..1f)
        assertTrue(result.y in 0f..1f)
        assertTrue(result.x > 0.99f)
    }

    @Test fun `ruido y valores invalidos no provocan saltos`() {
        val filter = HoloTiltFilter()
        filter.update(0f, 0f)
        assertEquals(HoloTilt(), filter.update(0.001f, -0.001f))
        assertEquals(HoloTilt(), filter.update(Float.NaN, Float.POSITIVE_INFINITY))
    }

    @Test fun `recalibracion y cruce angular evitan saltos`() {
        val filter = HoloTiltFilter()
        filter.update(3.14f, 3.14f)
        assertEquals(HoloTilt(), filter.update(-3.14f, -3.14f))
        filter.update(2f, 2f)
        filter.reset()
        assertEquals(HoloTilt(), filter.update(2f, 2f))
    }
}
