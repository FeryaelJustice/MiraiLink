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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var heartbeatJob: Job? = null

    private val vibrator: Vibrator? by lazy {
        vibratorOverride ?: run {
            val vibratorService = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator ?: vibratorService
            } else {
                vibratorService
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

        stopHeartbeat()

        val clampedRatio = affinityRatio.coerceIn(0.0f, 1.0f)
        // Frequency: 60 BPM (low affinity) to 115 BPM (high affinity)
        val bpm = 60 + (clampedRatio * 55f).roundToInt()
        val totalPeriodMs = (60_000L / bpm).coerceAtLeast(380L)

        // Lub (S1) and Dub (S2) durations
        val s1DurationMs = 50L
        val intervalMs = 85L
        val s2DurationMs = 35L
        val activeMs = s1DurationMs + intervalMs + s2DurationMs
        val pauseMs = (totalPeriodMs - activeMs).coerceAtLeast(60L)

        // Modulate amplitude according to affinity
        val minAmp = 90
        val maxAmp = 255
        val s1Amp = (minAmp + clampedRatio * (maxAmp - minAmp)).roundToInt().coerceIn(1, 255)
        val s2Amp = ((minAmp * 0.65f) + clampedRatio * (maxAmp * 0.70f - minAmp * 0.65f)).roundToInt().coerceIn(1, 255)

        // Emit the first pulse synchronously so feedback is immediate on touch and tests pass synchronously
        emitPulse(s1DurationMs, s1Amp)

        // Launch coroutine loop generating heartbeats at the exact BPM frequency
        heartbeatJob = scope.launch {
            Log.d(TAG, "startHeartbeat: active at $bpm BPM, period=${totalPeriodMs}ms, ratio=$clampedRatio")
            // Complete first beat cycle
            delay(s1DurationMs + intervalMs)
            if (!isActive) return@launch
            emitPulse(s2DurationMs, s2Amp)
            delay(s2DurationMs + pauseMs)

            // Loop subsequent beats
            while (isActive) {
                // S1 pulse ("Lub")
                emitPulse(s1DurationMs, s1Amp)
                delay(s1DurationMs + intervalMs)
                if (!isActive) break

                // S2 pulse ("Dub")
                emitPulse(s2DurationMs, s2Amp)
                delay(s2DurationMs + pauseMs)
            }
        }
    }

    override fun stopHeartbeat() {
        try {
            heartbeatJob?.cancel()
            heartbeatJob = null
            vibrator?.cancel()
            Log.d(TAG, "stopHeartbeat: stopped coroutine job and vibrator")
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
            emitPulse(140L, 255)
            Log.d(TAG, "triggerLikeConfirmation: confirmation pulse emitted")
        } catch (e: Exception) {
            Log.e(TAG, "triggerLikeConfirmation failed", e)
        }
    }

    private fun emitPulse(durationMs: Long, amplitude: Int) {
        val targetVibrator = vibrator ?: return
        if (!targetVibrator.hasVibrator()) return

        var success = false
        try {
            val effect = if (targetVibrator.hasAmplitudeControl()) {
                VibrationEffect.createOneShot(durationMs, amplitude.coerceIn(1, 255))
            } else {
                VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
            }
            vibrateTarget(targetVibrator, effect, durationMs)
            success = true
        } catch (e: Exception) {
            Log.w(TAG, "emitPulse VibrationEffect failed: ${e.message}")
        }

        if (!success) {
            try {
                @Suppress("DEPRECATION")
                targetVibrator.vibrate(durationMs)
            } catch (e: Exception) {
                Log.e(TAG, "emitPulse legacy vibrate failed: ${e.message}")
            }
        }
    }

    private fun vibrateTarget(vibrator: Vibrator, effect: VibrationEffect, durationMs: Long) {
        if (vibratorOverride != null) {
            vibrator.vibrate(effect)
            return
        }

        var dispatched = false
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val attrs = VibrationAttributes.Builder()
                    .setUsage(VibrationAttributes.USAGE_MEDIA)
                    .build()
                vibrator.vibrate(effect, attrs)
                dispatched = true
            } else {
                @Suppress("DEPRECATION")
                val audioAttrs = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .build()
                vibrator.vibrate(effect, audioAttrs)
                dispatched = true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibrate with attributes failed: ${e.message}")
        }

        if (!dispatched) {
            try {
                vibrator.vibrate(effect)
                dispatched = true
            } catch (e: Exception) {
                Log.w(TAG, "1-arg vibrate failed: ${e.message}")
            }
        }

        if (!dispatched) {
            try {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            } catch (e: Exception) {
                Log.e(TAG, "Legacy vibrate failed: ${e.message}")
            }
        }
    }
}
