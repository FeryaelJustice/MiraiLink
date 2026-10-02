package com.feryaeljustice.mirailink.data.studio

import android.graphics.Bitmap
import android.graphics.Color
import com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics
import kotlin.math.abs

class QualityMetricsCalculator {

    /**
     * Muestrea luminancia y varianza del bitmap para evaluar calidad sin procesar todos los píxeles.
     * La detección de screenshot es heurística de nombre/proporción/color, no validación de identidad.
     */
    fun calculateFromBitmap(
        bitmap: Bitmap,
        sourceNameOrUri: String? = null,
    ): ImageQualityMetrics {
        val width = bitmap.width
        val height = bitmap.height

        val stepX = (width / 64).coerceAtLeast(1)
        val stepY = (height / 64).coerceAtLeast(1)

        var totalLuminance = 0.0
        var sampleCount = 0

        val luminanceSamples = ArrayList<Double>((width / stepX) * (height / stepY) + 1)

        for (y in 0 until height step stepY) {
            for (x in 0 until width step stepX) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)

                // Formula ITU-R BT.601 para luminancia percibida
                val lum = 0.299 * r + 0.587 * g + 0.114 * b
                totalLuminance += lum
                luminanceSamples.add(lum)
                sampleCount++
            }
        }

        val avgLuminance = if (sampleCount > 0) totalLuminance / sampleCount else 128.0
        val avgPercent = ((avgLuminance / 255.0) * 100.0).toInt().coerceIn(0, 100)

        // Calculo de varianza / contraste
        var varianceSum = 0.0
        for (lum in luminanceSamples) {
            val diff = lum - avgLuminance
            varianceSum += diff * diff
        }
        val variance = if (sampleCount > 0) (varianceSum / sampleCount).toFloat() else 0f

        // Deteccion heuristica de captura de pantalla
        val (isScreenshot, reason) = detectScreenshot(width, height, sourceNameOrUri, bitmap)

        return ImageQualityMetrics(
            averageLuminancePercent = avgPercent,
            contrastVariance = variance,
            width = width,
            height = height,
            isLikelyScreenshot = isScreenshot,
            screenshotReason = reason,
        )
    }

    private fun detectScreenshot(
        width: Int,
        height: Int,
        sourceName: String?,
        bitmap: Bitmap,
    ): Pair<Boolean, String?> {
        // 1. Deteccion por nombre de archivo
        if (sourceName != null) {
            val lower = sourceName.lowercase()
            if (lower.contains("screenshot") || lower.contains("captura") || lower.contains("screen_shot")) {
                return true to "El nombre del archivo coincide con el patron estandar de captura de pantalla."
            }
        }

        // 2. Relacion de aspecto de pantalla movil (ej. 19.5:9, 20:9, 16:9 exacta)
        val aspectRatio = if (width > 0 && height > 0) {
            height.toFloat() / width.toFloat()
        } else {
            1f
        }

        if (aspectRatio >= 2.05f || aspectRatio <= 0.48f) {
            // Comprobar si la franja superior (posible status bar) tiene color solido uniforme
            val topBarUniform = isTopBarUniform(bitmap, width, height)
            if (topBarUniform) {
                return true to "Relacion de aspecto vertical extrema con barra superior fija de sistema."
            }
        }

        return false to null
    }

    private fun isTopBarUniform(bitmap: Bitmap, width: Int, height: Int): Boolean {
        if (height < 50 || width < 50) return false
        val sampleY = (height * 0.02f).toInt().coerceIn(2, height - 1)
        val firstPixel = bitmap.getPixel(width / 4, sampleY)
        val midPixel = bitmap.getPixel(width / 2, sampleY)
        val thirdPixel = bitmap.getPixel((width * 3) / 4, sampleY)

        val diff1 = abs(Color.red(firstPixel) - Color.red(midPixel)) +
            abs(Color.green(firstPixel) - Color.green(midPixel)) +
            abs(Color.blue(firstPixel) - Color.blue(midPixel))

        val diff2 = abs(Color.red(midPixel) - Color.red(thirdPixel)) +
            abs(Color.green(midPixel) - Color.green(thirdPixel)) +
            abs(Color.blue(midPixel) - Color.blue(thirdPixel))

        return diff1 < 15 && diff2 < 15
    }

    /**
     * Muestrea el plano Y usando rowStride y pixelStride; el índice no supone memoria compacta.
     * Se usa en frames CameraX para evitar crear un bitmap por cada cálculo de luminancia.
     */
    fun calculateLuminanceFromYPlane(
        yBuffer: java.nio.ByteBuffer,
        width: Int,
        height: Int,
        pixelStride: Int,
        rowStride: Int,
    ): Int {
        var totalLuminance = 0L
        var count = 0
        val step = 16

        for (row in 0 until height step step) {
            val rowStart = row * rowStride
            for (col in 0 until width step step) {
                val index = rowStart + (col * pixelStride)
                if (index < yBuffer.limit()) {
                    val yVal = yBuffer.get(index).toInt() and 0xFF
                    totalLuminance += yVal
                    count++
                }
            }
        }

        if (count == 0) return 50
        val avg = (totalLuminance / count).toDouble()
        return ((avg / 255.0) * 100.0).toInt().coerceIn(0, 100)
    }
}
