package com.feryaeljustice.mirailink.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.domain.model.User
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeCardStack(
    users: List<User>,
    canUndo: Boolean,
    onSwipeLeft: () -> Unit,
    onGoBack: (() -> Unit),
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (users.isEmpty()) return

    val scope = rememberCoroutineScope()

    val topUser = users.first()
    val nextUser = users.getOrNull(1)

    var offsetX = remember { Animatable(0f) }
    var offsetY = remember { Animatable(0f) }
    val rotation = (offsetX.value / 60).coerceIn(-40f, 40f)
    val alphaAnim by animateFloatAsState(targetValue = 1 - (abs(offsetX.value) / 1000f))

    Box(modifier = modifier) {

        // Muestra la siguiente card detrás con menos opacidad
        nextUser?.let {
            UserCard(
                user = it,
                modifier = Modifier
                    .padding(2.dp)
                    .alpha(0.5f)
            )
        }

        // Card interactiva al frente
        UserCard(
            user = topUser,
            canUndo = canUndo,
            modifier = Modifier
                .padding(2.dp)
                .graphicsLayer(
                    translationX = offsetX.value,
                    translationY = offsetY.value,
                    rotationZ = rotation
                )
                .alpha(alphaAnim)
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
                        }
                    )
                },
            isPreviewMode = false,
            onLike = { onSwipeRight() },
            onGoBackToLast = { onGoBack() },
            onDislike = { onSwipeLeft() },
        )
    }
}