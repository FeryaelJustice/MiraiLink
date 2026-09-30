package com.feryaeljustice.mirailink.ui.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.VisibleForTesting
import kotlin.math.roundToInt

interface HapticHeartbeatController {
    val isSupported: Boolean
    fun startHeartbeat(affinityRatio: Float)
    fun stopHeartbeat()
    fun triggerLikeConfirmation()
}

class HapticHeartbeatControllerImpl(
    private val context: Context,
    @get:VisibleForTesting internal val vibratorOverride: Vibrator? = null,
) : HapticHeartbeatController {

    private val vibrator: Vibrator? by lazy {
        vibratorOverride ?: run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        }
    }

    override val isSupported: Boolean
        get() = vibrator?.hasVibrator() == true

    override fun startHeartbeat(affinityRatio: Float) {
        val targetVibrator = vibrator ?: return
        if (!targetVibrator.hasVibrator()) return

        val clampedRatio = affinityRatio.coerceIn(0.0f, 1.0f)
        val bpm = 60 + (clampedRatio * 55f).roundToInt()
        val totalPeriodMs = (60_000f / bpm).roundToInt().coerceAtLeast(400)

        val s1DurationMs = 70L
        val intervalMs = 110L
        val s2DurationMs = 50L
        val activeMs = s1DurationMs + intervalMs + s2DurationMs
        val pauseMs = (totalPeriodMs - activeMs).coerceAtLeast(80L)

        val timings = longArrayOf(0L, s1DurationMs, intervalMs, s2DurationMs, pauseMs)

        try {
            if (targetVibrator.hasAmplitudeControl()) {
                val minAmp = 70
                val maxAmp = 255
                val s1Amp = (minAmp + clampedRatio * (maxAmp - minAmp)).roundToInt().coerceIn(1, 255)
                val s2Amp = ((minAmp * 0.7f) + clampedRatio * (maxAmp * 0.75f - minAmp * 0.7f)).roundToInt().coerceIn(1, 255)

                val amplitudes = intArrayOf(0, s1Amp, 0, s2Amp, 0)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, 0)
                targetVibrator.vibrate(effect)
            } else {
                val effect = VibrationEffect.createWaveform(timings, 0)
                targetVibrator.vibrate(effect)
            }
        } catch (_: Exception) {
        }
    }

    override fun stopHeartbeat() {
        try {
            vibrator?.cancel()
        } catch (_: Exception) {
        }
    }

    override fun triggerLikeConfirmation() {
        val targetVibrator = vibrator ?: return
        if (!targetVibrator.hasVibrator()) return

        stopHeartbeat()

        try {
            val effect = VibrationEffect.createOneShot(100L, 255)
            targetVibrator.vibrate(effect)
        } catch (_: Exception) {
        }
    }
}
