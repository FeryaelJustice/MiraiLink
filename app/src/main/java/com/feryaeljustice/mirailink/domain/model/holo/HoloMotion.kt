package com.feryaeljustice.mirailink.domain.model.holo

import kotlin.math.abs

/** Coordenadas normalizadas. No representan acciones ni afinidad de usuario. */
data class HoloTilt(val x: Float = 0f, val y: Float = 0f)

enum class HoloRenderMode { Static, SimpleParallax, SegmentedParallax }

data class HoloActivation(
    val enabled: Boolean,
    val resumed: Boolean,
    val focused: Boolean,
    val deviceAllowsMotion: Boolean,
    val sensorAvailable: Boolean,
    val interacting: Boolean = false,
    val covered: Boolean = false,
) {
    val active: Boolean get() = enabled && resumed && focused && deviceAllowsMotion &&
        sensorAvailable && !interacting && !covered
}

/** Calibracion, suavizado y zona muerta independientes de Android. */
class HoloTiltFilter {
    private var neutral: HoloTilt? = null
    private var previous = HoloTilt()

    fun reset() {
        neutral = null
        previous = HoloTilt()
    }

    fun update(pitch: Float, roll: Float): HoloTilt {
        if (!pitch.isFinite() || !roll.isFinite()) return previous
        val origin = neutral ?: HoloTilt(pitch, roll).also { neutral = it }
        fun normalize(angle: Float): Float {
            var delta = angle % (Math.PI * 2).toFloat()
            if (delta > Math.PI) delta -= (Math.PI * 2).toFloat()
            if (delta < -Math.PI) delta += (Math.PI * 2).toFloat()
            return if (abs(delta) < 0.012f) 0f else (delta / 0.26f).coerceIn(-1f, 1f)
        }
        val target = HoloTilt(normalize(roll - origin.y), normalize(pitch - origin.x))
        previous = HoloTilt(
            previous.x + 0.18f * (target.x - previous.x),
            previous.y + 0.18f * (target.y - previous.y),
        )
        return previous
    }
}
