package com.feryaeljustice.mirailink.ui.components.user

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.launch
import kotlin.math.abs

@Suppress("ktlint:standard:function-naming")
@Composable
fun UserSwipeCardStack(
    users: List<UserViewEntry>,
    canUndo: Boolean,
    onSwipeLeft: () -> Unit,
    onGoBack: (() -> Unit),
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (users.isEmpty()) return

    val scope = rememberCoroutineScope()

    val topUser = users.first()
    val nextUser = users.getOrNull(1)

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = (offsetX.value / 60).coerceIn(-40f, 40f)
    val alphaAnim by animateFloatAsState(targetValue = 1 - (abs(offsetX.value) / 1000f))

    Box(modifier = modifier) {
        // Muestra la siguiente card detrás con menos opacidad
        nextUser?.let {
            UserCard(
                modifier =
                    Modifier
                        .padding(2.dp)
                        .alpha(0.5f),
                user = it,
                onSave = {},
            )
        }

        // Card interactiva al frente
        UserCard(
            modifier =
                Modifier
                    .padding(2.dp)
                    .graphicsLayer(
                        translationX = offsetX.value,
                        translationY = offsetY.value,
                        rotationZ = rotation,
                    ).graphicsLayer { alpha = alphaAnim }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                when {
                                    offsetX.value > 300f -> {
                                        scope.launch {
                                            offsetX.animateTo(1000f)
                                            onSwipeRight()
                                            offsetX.snapTo(0f)
                                            offsetY.snapTo(0f)
                                        }
                                    }

                                    offsetX.value < -300f -> {
                                        scope.launch {
                                            offsetX.animateTo(-1000f)
                                            onSwipeLeft()
                                            offsetX.snapTo(0f)
                                            offsetY.snapTo(0f)
                                        }
                                    }

                                    else -> {
                                        scope.launch {
                                            offsetX.animateTo(0f)
                                            offsetY.animateTo(0f)
                                        }
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                }
                            },
                        )
                    },
            user = topUser,
            canUndo = canUndo,
            onSave = {},
            isPreviewMode = false,
            onLike = { onSwipeRight() },
            onGoBackToLast = { onGoBack() },
            onDislike = { onSwipeLeft() },
        )
    }
}
