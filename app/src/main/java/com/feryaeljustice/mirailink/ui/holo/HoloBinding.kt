package com.feryaeljustice.mirailink.ui.holo

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/** Observador independiente: no consume pulsaciones ni compite con detectores existentes. */
fun Modifier.observeHoloTouch(controller: HoloRenderController?): Modifier =
    if (controller == null) this else pointerInput(controller) {
        try {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                controller.setPressed(true)
                try {
                    do {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                    } while (event.changes.any { it.pressed })
                } finally {
                    controller.setPressed(false)
                }
            }
        } finally {
            controller.setPressed(false)
        }
    }

@Composable
fun BindHoloController(controller: HoloRenderController?, moving: Boolean) {
    if (controller == null) return
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var resumed by remember(lifecycle) { mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) }
    val focused = LocalWindowInfo.current.isWindowFocused
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, _ ->
            resumed = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(controller, moving) { controller.setMoving(moving) }
    LaunchedEffect(controller, resumed, focused) {
        controller.bind(resumed, focused, moving = false)
    }
}
