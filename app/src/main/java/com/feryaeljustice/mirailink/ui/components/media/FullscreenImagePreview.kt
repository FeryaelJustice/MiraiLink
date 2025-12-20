package com.feryaeljustice.mirailink.ui.components.media

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
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
    backgroundAlpha: Float = 0.6f,
    contentScale: ContentScale = ContentScale.Fit,
    contentPadding: Dp = 8.dp,
) {
    val currentOnDismiss by rememberUpdatedState(newValue = onDismiss)
    // Para hacer pinch to zoom
    // Tamaño del área de imagen (contenedor)
    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    // Estado transformable (animable)
    val scale = remember { Animatable(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val rotation = remember { Animatable(0f) } // grados
    val scope = rememberCoroutineScope()

    val minScale = 1f
    val maxScale = 5f

    suspend fun animateToIdentity() {
        val d = 220
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

    val transformState =
        rememberTransformableState { zoomChange, panChange, rotationChange ->
            // Zoom con límites
            val targetScale = (scale.value * zoomChange).coerceIn(minScale, maxScale)
            if (targetScale != scale.value) {
                scope.launch {
                    scale.snapTo(targetScale)
                    // Al cambiar escala, re-clamp del offset
                    offset.snapTo(clampOffset(offset.value, scale.value, contentSize))
                }
            }

            // Pan si hay zoom > 1
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

            // Rotación con dos dedos
            if (rotationChange != 0f) {
                scope.launch {
                    val newRotation = normalizeDeg(rotation.value + rotationChange)
                    rotation.snapTo(newRotation)
                }
            }
        }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = backgroundAlpha))
                .clickable { currentOnDismiss() } // cierra al pulsar fuera
                .padding(contentPadding)
                .zIndex(99f),
        contentAlignment = Alignment.Center,
    ) {
        // Botón cerrar
        MiraiLinkIconButton(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .zIndex(100f),
            onClick = {
                scope
                    .launch { animateToIdentity() }
                    .invokeOnCompletion { currentOnDismiss() }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = closeContentDescription,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Imagen con zoom + pan + doble toque
        AsyncImage(
            model = imageUrl,
            contentDescription = imageContentDescription,
            contentScale = contentScale,
            modifier =
                Modifier
                    .fillMaxSize(0.9f)
                    .clickable(enabled = false) {}
                    .padding(all = contentPadding)
                    .onSizeChanged { size ->
                        contentSize = size
                        // Re-clamp si cambia el tamaño del contenedor
                        scope.launch {
                            offset.snapTo(clampOffset(offset.value, scale.value, size))
                        }
                    }.pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { tapOffset ->
                                scope.launch {
                                    if (scale.value < 2f) {
                                        val newScale = 2f
                                        // Centrar hacia el punto del doble toque
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
                    }.graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        translationX = offset.value.x
                        translationY = offset.value.y
                        rotationZ = rotation.value
                        transformOrigin = TransformOrigin.Center
                    }.transformable(transformState),
        )
    }
}
