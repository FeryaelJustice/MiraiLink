package com.feryaeljustice.mirailink.ui.haptics

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HapticHeartbeatControllerTest {

    private val context = mockk<Context>(relaxed = true)
    private val vibrator = mockk<Vibrator>(relaxed = true)

    @Test
    fun `isSupported reflects vibrator hasVibrator value`() {
        every { vibrator.hasVibrator() } returns true
        val controllerSupported = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        assertThat(controllerSupported.isSupported).isTrue()

        every { vibrator.hasVibrator() } returns false
        val controllerUnsupported = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        assertThat(controllerUnsupported.isSupported).isFalse()
    }

    @Test
    fun `startHeartbeat calls vibrate when amplitude control is supported`() {
        every { vibrator.hasVibrator() } returns true
        every { vibrator.hasAmplitudeControl() } returns true
        every { vibrator.vibrate(any<VibrationEffect>()) } just runs

        val controller = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        controller.startHeartbeat(0.85f)

        verify(atLeast = 1) { vibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun `startHeartbeat calls vibrate fallback when amplitude control is not supported`() {
        every { vibrator.hasVibrator() } returns true
        every { vibrator.hasAmplitudeControl() } returns false
        every { vibrator.vibrate(any<VibrationEffect>()) } just runs

        val controller = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        controller.startHeartbeat(0.2f)

        verify(atLeast = 1) { vibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun `startHeartbeat does nothing if hardware is not supported`() {
        every { vibrator.hasVibrator() } returns false

        val controller = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        controller.startHeartbeat(0.5f)

        verify(exactly = 0) { vibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun `stopHeartbeat cancels vibrator`() {
        val controller = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        controller.stopHeartbeat()

        verify { vibrator.cancel() }
    }

    @Test
    fun `triggerLikeConfirmation vibrates with confirmation pulse`() {
        every { vibrator.hasVibrator() } returns true
        every { vibrator.vibrate(any<VibrationEffect>()) } just runs

        val controller = HapticHeartbeatControllerImpl(context, vibratorOverride = vibrator)
        controller.triggerLikeConfirmation()

        verify { vibrator.cancel() }
        verify(atLeast = 1) { vibrator.vibrate(any<VibrationEffect>()) }
    }
}
