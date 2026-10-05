package com.feryaeljustice.mirailink.core.holo

import kotlin.math.ceil
import kotlin.math.floor

/** Mascara local en coordenadas de imagen. No clasifica anime frente a personas. */
class HoloMask(val width: Int, val height: Int, val confidence: FloatArray) {
    fun sample(x: Float, y: Float): Float {
        if (x < 0f || y < 0f || x > width - 1f || y > height - 1f) return 0f
        val ix = floor(x).toInt()
        val iy = floor(y).toInt()
        val nx = (ix + 1).coerceAtMost(width - 1)
        val ny = (iy + 1).coerceAtMost(height - 1)
        val fx = x - ix
        val fy = y - iy
        val top = confidence[iy * width + ix] * (1f - fx) + confidence[iy * width + nx] * fx
        val bottom = confidence[ny * width + ix] * (1f - fx) + confidence[ny * width + nx] * fx
        return top * (1f - fy) + bottom * fy
    }

    fun alpha(value: Float): Float {
        val t = ((value - 0.2f) / 0.6f).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }
}

object HoloMaskCoverage {
    const val ForegroundScale = 1.035f

    /** Rechaza mascaras degeneradas y desplazamientos que descubren el sujeto original. */
    fun supportsDepth(mask: HoloMask, maxShiftX: Float, maxShiftY: Float): Boolean {
        if (mask.width < 2 || mask.height < 2 || mask.confidence.size != mask.width * mask.height ||
            !maxShiftX.isFinite() || !maxShiftY.isFinite() || maxShiftX < 0f || maxShiftY < 0f ||
            mask.confidence.any { !it.isFinite() || it !in 0f..1f }
        ) return false
        val foreground = mask.confidence.count { it >= 0.8f }
        val background = mask.confidence.count { it <= 0.2f }
        if (foreground < mask.confidence.size * 0.03f || background < mask.confidence.size * 0.05f) return false
        val cx = (mask.width - 1) / 2f
        val cy = (mask.height - 1) / 2f
        if (maxShiftX > 8f || maxShiftY > 8f) return false
        // Cada interpolacion bilineal queda acotada por sus vecinos. Revisar su minimo
        // cubre el rectangulo continuo y evita miles de interpolaciones por pixel.
        for (y in 0 until mask.height) for (x in 0 until mask.width) {
            val original = mask.confidence[y * mask.width + x]
            if (original < 0.5f) continue
            val left = floor((x - cx - maxShiftX) / ForegroundScale + cx).toInt()
            val right = ceil((x - cx + maxShiftX) / ForegroundScale + cx).toInt()
            val top = floor((y - cy - maxShiftY) / ForegroundScale + cy).toInt()
            val bottom = ceil((y - cy + maxShiftY) / ForegroundScale + cy).toInt()
            if (left < 0 || right >= mask.width || top < 0 || bottom >= mask.height) return false
            val minimumAlpha = mask.alpha(original) - 0.05f
            for (sy in top..bottom) for (sx in left..right) {
                if (mask.alpha(mask.confidence[sy * mask.width + sx]) < minimumAlpha) return false
            }
        }
        return true
    }
}
