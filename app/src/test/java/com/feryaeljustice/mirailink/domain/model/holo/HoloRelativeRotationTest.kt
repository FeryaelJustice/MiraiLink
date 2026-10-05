package com.feryaeljustice.mirailink.domain.model.holo

import org.junit.Assert.*
import org.junit.Test

class HoloRelativeRotationTest {
    @Test fun `postura vertical calibra identidad y conserva inclinacion relativa`() {
        val rotation = HoloRelativeRotation()
        val vertical = floatArrayOf(1f, 0f, 0f, 0f, 0f, -1f, 0f, 1f, 0f)
        val result = FloatArray(9)
        assertTrue(rotation.relativeToNeutral(vertical, result))
        assertArrayEquals(identity(), result, 0.0001f)
        // Neutral * giro de 90 grados alrededor de Y.
        val turned = floatArrayOf(0f, 0f, 1f, 1f, 0f, 0f, 0f, 1f, 0f)
        rotation.relativeToNeutral(turned, result)
        assertArrayEquals(floatArrayOf(0f, 0f, 1f, 0f, 1f, 0f, -1f, 0f, 0f), result, 0.0001f)
        rotation.reset()
        rotation.relativeToNeutral(turned, result)
        assertArrayEquals(identity(), result, 0.0001f)
    }

    @Test fun `matriz invalida no altera calibracion`() {
        val rotation = HoloRelativeRotation()
        val result = FloatArray(9)
        assertFalse(rotation.relativeToNeutral(FloatArray(9) { Float.NaN }, result))
        assertTrue(rotation.relativeToNeutral(identity(), result))
        assertArrayEquals(identity(), result, 0f)
    }

    private fun identity() = floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
}
