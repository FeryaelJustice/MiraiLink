package com.feryaeljustice.mirailink.ui.components.haptics

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun Modifier.hapticHeartbeatLikeTrigger(
    onTap: () -> Unit,
    onHoldStart: () -> Unit,
    onHoldProgress: (Float) -> Unit,
    onHoldComplete: () -> Unit,
    onHoldCancel: () -> Unit,
    holdDurationMs: Long = 1200L,
    initialHoldDelayMs: Long = 180L,
): Modifier {
    val scope = rememberCoroutineScope()

    return this.pointerInput(onTap, onHoldStart, onHoldComplete, onHoldCancel) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            val downTime = System.currentTimeMillis()
            var isHeld = false

            val holdJob: Job = scope.launch {
                delay(initialHoldDelayMs)
                isHeld = true
                onHoldStart()
                val startHoldTime = System.currentTimeMillis()
                while (isActive) {
                    val elapsed = System.currentTimeMillis() - startHoldTime
                    val progress = (elapsed.toFloat() / holdDurationMs).coerceIn(0f, 1f)
                    onHoldProgress(progress)
                    delay(16)
                }
            }

            val upOrCancel = waitForUpOrCancellation()
            holdJob.cancel()

            val totalDuration = System.currentTimeMillis() - downTime
            if (upOrCancel != null && !upOrCancel.isConsumed) {
                upOrCancel.consume()
                if (isHeld) {
                    val progress = ((totalDuration - initialHoldDelayMs).toFloat() / holdDurationMs).coerceIn(0f, 1f)
                    if (progress >= 1f) {
                        onHoldComplete()
                    } else {
                        onHoldCancel()
                    }
                } else {
                    onTap()
                }
            } else {
                if (isHeld) {
                    onHoldCancel()
                }
            }
        }
    }
}
