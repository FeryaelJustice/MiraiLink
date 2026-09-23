package com.feryaeljustice.mirailink.ui.components.media

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.ui.utils.clampOffset
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun FullscreenImagePreview(
    imageUrl: String,
    onDismiss: () -> Unit,
    closeContentDescription: String,
    imageContentDescription: String,
    modifier: Modifier = Modifier,
    backgroundAlpha: Float = 0.90f,
    contentScale: ContentScale = ContentScale.Fit,
    contentPadding: Dp = 16.dp,
) {
    val currentOnDismiss by rememberUpdatedState(newValue = onDismiss)

    var isClosing by remember { mutableStateOf(false) }
    val transitionAlpha = remember { Animatable(0f) }
    val transitionScale = remember { Animatable(0.92f) }

    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    val scale = remember { Animatable(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val minScale = 1f
    val maxScale = 5f

    suspend fun animateToIdentity() {
        val d = 200
        scale.animateTo(1f, tween(d))
        offset.animateTo(Offset.Zero, tween(d))
        rotation.animateTo(0f, tween(d))
    }

    fun normalizeDeg(deg: Float): Float {
        var d = deg % 360f
        if (d > 180f) d -= 360f
        if (d < -180f) d += 360f
        return d
    }

    LaunchedEffect(Unit) {
        launch {
            transitionAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 260),
            )
        }
        launch {
            transitionScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 260),
            )
        }
    }

    val handleDismiss: () -> Unit = {
        if (!isClosing) {
            isClosing = true
            scope.launch {
                launch {
                    transitionAlpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 200),
                    )
                }
                launch {
                    transitionScale.animateTo(
                        targetValue = 0.92f,
                        animationSpec = tween(durationMillis = 200),
                    )
                }
                animateToIdentity()
                currentOnDismiss()
            }
        }
    }

    val transformState =
        rememberTransformableState { _, zoomChange, panChange, rotationChange ->
            val targetScale = (scale.value * zoomChange).coerceIn(minScale, maxScale)
            if (targetScale != scale.value) {
                scope.launch {
                    scale.snapTo(targetScale)
                    offset.snapTo(clampOffset(offset.value, scale.value, contentSize))
                }
            }

            if (scale.value > 1f) {
                scope.launch {
                    val newOffset = offset.value + panChange
                    offset.snapTo(clampOffset(newOffset, scale.value, contentSize))
                }
            } else {
                if (offset.value != Offset.Zero) {
                    scope.launch { offset.snapTo(Offset.Zero) }
                }
            }

            if (rotationChange != 0f) {
                scope.launch {
                    val newRotation = normalizeDeg(rotation.value + rotationChange)
                    rotation.snapTo(newRotation)
                }
            }
        }

    Dialog(
        onDismissRequest = handleDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = backgroundAlpha * transitionAlpha.value))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { handleDismiss() }
                    .zIndex(99f),
            contentAlignment = Alignment.Center,
        ) {
            // Photo container with rounded corners, aesthetic border, and elevation shadow
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.35f)),
                shadowElevation = 20.dp,
                modifier =
                    Modifier
                        .wrapContentSize()
                        .padding(contentPadding)
                        .graphicsLayer {
                            val combinedScale = scale.value * transitionScale.value
                            scaleX = combinedScale
                            scaleY = combinedScale
                            translationX = offset.value.x
                            translationY = offset.value.y
                            rotationZ = rotation.value
                            alpha = transitionAlpha.value
                            transformOrigin = TransformOrigin.Center
                        }
                        .transformable(transformState)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { /* Consume taps on the photo itself */ },
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = imageContentDescription,
                    contentScale = contentScale,
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .onSizeChanged { size ->
                                contentSize = size
                                scope.launch {
                                    offset.snapTo(clampOffset(offset.value, scale.value, size))
                                }
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = { tapOffset ->
                                        scope.launch {
                                            if (scale.value < 2f) {
                                                val newScale = 2f
                                                if (contentSize != IntSize.Zero) {
                                                    val center =
                                                        Offset(
                                                            contentSize.width / 2f,
                                                            contentSize.height / 2f,
                                                        )
                                                    val scaleRatio = newScale / scale.value
                                                    val translated =
                                                        (offset.value + (tapOffset - center)) * scaleRatio
                                                    val newOffset = translated - (tapOffset - center)
                                                    offset.snapTo(
                                                        clampOffset(
                                                            newOffset,
                                                            newScale,
                                                            contentSize,
                                                        ),
                                                    )
                                                }
                                                scale.animateTo(newScale, tween(180))
                                            } else {
                                                animateToIdentity()
                                            }
                                        }
                                    },
                                    onLongPress = {
                                        scope.launch { animateToIdentity() }
                                    },
                                )
                            },
                )
            }

            // Stylized close button positioned with safe insets for any screen orientation
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.65f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                shadowElevation = 6.dp,
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(16.dp)
                        .size(44.dp)
                        .zIndex(100f)
                        .graphicsLayer {
                            alpha = transitionAlpha.value
                        },
                onClick = handleDismiss,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = closeContentDescription,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}
