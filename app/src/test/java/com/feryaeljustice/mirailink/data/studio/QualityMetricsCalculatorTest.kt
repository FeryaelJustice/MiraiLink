package com.feryaeljustice.mirailink.data.studio

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.nio.ByteBuffer

class QualityMetricsCalculatorTest {

    private lateinit var calculator: QualityMetricsCalculator

    @Before
    fun setUp() {
        calculator = QualityMetricsCalculator()
    }

    @Test
    fun `calculateLuminanceFromYPlane computes dark luminance correctly`() {
        val width = 32
        val height = 32
        val buffer = ByteBuffer.allocate(width * height)
        for (i in 0 until (width * height)) {
            buffer.put(25.toByte())
        }
        buffer.flip()

        val percent = calculator.calculateLuminanceFromYPlane(
            yBuffer = buffer,
            width = width,
            height = height,
            pixelStride = 1,
            rowStride = width,
        )

        assertThat(percent).isIn(8..12)
    }

    @Test
    fun `calculateLuminanceFromYPlane computes bright luminance correctly`() {
        val width = 32
        val height = 32
        val buffer = ByteBuffer.allocate(width * height)
        for (i in 0 until (width * height)) {
            buffer.put(230.toByte())
        }
        buffer.flip()

        val percent = calculator.calculateLuminanceFromYPlane(
            yBuffer = buffer,
            width = width,
            height = height,
            pixelStride = 1,
            rowStride = width,
        )

        assertThat(percent).isIn(88..92)
    }
}
