package com.feryaeljustice.mirailink.ui.haptics

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlin.math.roundToInt

private const val TAG = "HapticHeartbeat"

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
        val targetVibrator = vibrator ?: run {
            Log.w(TAG, "startHeartbeat: Vibrator service is null")
            return
        }
        if (!targetVibrator.hasVibrator()) {
            Log.w(TAG, "startHeartbeat: Device reports hasVibrator == false")
            return
        }

        val clampedRatio = affinityRatio.coerceIn(0.0f, 1.0f)
        val bpm = 60 + (clampedRatio * 55f).roundToInt()
        val totalPeriodMs = (60_000f / bpm).roundToInt().coerceAtLeast(400)

        val s1DurationMs = 70L
        val intervalMs = 110L
        val s2DurationMs = 50L
        val activeMs = s1DurationMs + intervalMs + s2DurationMs
        val pauseMs = (totalPeriodMs - activeMs).coerceAtLeast(80L)

        // All timings must be strictly positive (> 0) to comply with Android 12+ VibrationEffect validation
        val timings = longArrayOf(s1DurationMs, intervalMs, s2DurationMs, pauseMs)

        try {
            val amplitudes = if (targetVibrator.hasAmplitudeControl()) {
                val minAmp = 70
                val maxAmp = 255
                val s1Amp = (minAmp + clampedRatio * (maxAmp - minAmp)).roundToInt().coerceIn(1, 255)
                val s2Amp = ((minAmp * 0.7f) + clampedRatio * (maxAmp * 0.75f - minAmp * 0.7f)).roundToInt().coerceIn(1, 255)
                intArrayOf(s1Amp, 0, s2Amp, 0)
            } else {
                intArrayOf(
                    VibrationEffect.DEFAULT_AMPLITUDE,
                    0,
                    VibrationEffect.DEFAULT_AMPLITUDE,
                    0,
                )
            }

            val effect = VibrationEffect.createWaveform(timings, amplitudes, 0)
            vibrate(targetVibrator, effect)
            Log.d(TAG, "startHeartbeat: running bpm=$bpm, period=${totalPeriodMs}ms, ratio=$clampedRatio")
        } catch (e: Exception) {
            Log.e(TAG, "startHeartbeat failed to start waveform vibration", e)
            try {
                // Secondary fallback for devices with strict waveform restrictions:
                // Alternating off/on durations where index 0 is off duration
                val fallbackTimings = longArrayOf(pauseMs, s1DurationMs, intervalMs, s2DurationMs)
                val fallbackEffect = VibrationEffect.createWaveform(fallbackTimings, 0)
                vibrate(targetVibrator, fallbackEffect)
                Log.d(TAG, "startHeartbeat: fallback waveform succeeded")
            } catch (fallbackEx: Exception) {
                Log.e(TAG, "startHeartbeat fallback waveform also failed", fallbackEx)
            }
        }
    }

    override fun stopHeartbeat() {
        try {
            vibrator?.cancel()
            Log.d(TAG, "stopHeartbeat: vibrator cancelled")
        } catch (e: Exception) {
            Log.e(TAG, "stopHeartbeat failed", e)
        }
    }

    override fun triggerLikeConfirmation() {
        val targetVibrator = vibrator ?: run {
            Log.w(TAG, "triggerLikeConfirmation: Vibrator service is null")
            return
        }
        if (!targetVibrator.hasVibrator()) {
            Log.w(TAG, "triggerLikeConfirmation: Device reports hasVibrator == false")
            return
        }

        stopHeartbeat()

        try {
            val amplitude = if (targetVibrator.hasAmplitudeControl()) 255 else VibrationEffect.DEFAULT_AMPLITUDE
            val effect = VibrationEffect.createOneShot(120L, amplitude)
            vibrate(targetVibrator, effect)
            Log.d(TAG, "triggerLikeConfirmation: pulse emitted")
        } catch (e: Exception) {
            Log.e(TAG, "triggerLikeConfirmation failed", e)
        }
    }

    private fun vibrate(vibrator: Vibrator, effect: VibrationEffect) {
        if (vibratorOverride != null) {
            vibrator.vibrate(effect)
            return
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val attrs = VibrationAttributes.Builder()
                    .setUsage(VibrationAttributes.USAGE_TOUCH)
                    .build()
                vibrator.vibrate(effect, attrs)
            } else {
                @Suppress("DEPRECATION")
                val audioAttrs = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .build()
                vibrator.vibrate(effect, audioAttrs)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibrate with attributes failed, falling back to basic vibrate", e)
            vibrator.vibrate(effect)
        }
    }
}
