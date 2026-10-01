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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

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

    val currentOnTap by rememberUpdatedState(onTap)
    val currentOnHoldStart by rememberUpdatedState(onHoldStart)
    val currentOnHoldProgress by rememberUpdatedState(onHoldProgress)
    val currentOnHoldComplete by rememberUpdatedState(onHoldComplete)
    val currentOnHoldCancel by rememberUpdatedState(onHoldCancel)

    return this.pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            val downTime = System.currentTimeMillis()
            var isHeld = false
            var completed = false

            val holdJob: Job = scope.launch {
                delay(initialHoldDelayMs)
                isHeld = true
                currentOnHoldStart()
                val startHoldTime = System.currentTimeMillis()
                while (isActive) {
                    val elapsed = System.currentTimeMillis() - startHoldTime
                    val progress = (elapsed.toFloat() / holdDurationMs).coerceIn(0f, 1f)
                    currentOnHoldProgress(progress)
                    if (progress >= 1f) {
                        completed = true
                        currentOnHoldComplete()
                        break
                    }
                    delay(16)
                }
            }

            try {
                val upOrCancel = waitForUpOrCancellation()
                holdJob.cancel()

                val totalDuration = System.currentTimeMillis() - downTime
                if (upOrCancel != null && !upOrCancel.isConsumed) {
                    upOrCancel.consume()
                    if (completed) {
                        // Already completed when progress reached 100%
                    } else if (isHeld) {
                        val progress = ((totalDuration - initialHoldDelayMs).toFloat() / holdDurationMs).coerceIn(0f, 1f)
                        if (progress >= 1f) {
                            currentOnHoldComplete()
                        } else {
                            currentOnHoldCancel()
                        }
                    } else {
                        currentOnTap()
                    }
                } else {
                    if (!completed && isHeld) {
                        currentOnHoldCancel()
                    }
                }
            } finally {
                holdJob.cancel()
            }
        }
    }
}
