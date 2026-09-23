package com.feryaeljustice.mirailink.ui.components.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry
import kotlin.math.roundToInt

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditablePhotoGrid(
    photos: List<PhotoSlotViewEntry>,
    onSlotClick: ((Int) -> Unit)?,
    onPhotoReorder: ((Int, Int) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val currentSlotClick by rememberUpdatedState(newValue = onSlotClick)
    val currentPhotoReorder by rememberUpdatedState(newValue = onPhotoReorder)

    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var targetHoverIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    val filledCount = photos.count { it.url != null || it.uri != null }

    val previewPhotos =
        remember(photos, draggedIndex, targetHoverIndex) {
            val from = draggedIndex
            val to = targetHoverIndex
            if (from != null && to != null && from != to && from in photos.indices && to in photos.indices) {
                val swapped = photos.toMutableList()
                val temp = swapped[from]
                swapped[from] = swapped[to].copy(position = from)
                swapped[to] = temp.copy(position = to)
                swapped
            } else {
                photos
            }
        }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val spacingPx = with(density) { 12.dp.toPx() }
        val totalWidthPx = constraints.maxWidth.toFloat()
        val cellSizePx = (totalWidthPx - spacingPx) / 2f
        val cellSizeDp = with(density) { cellSizePx.toDp() }

        fun getSlotCenter(index: Int): Offset {
            val row = index / 2
            val col = index % 2
            val x = col * (cellSizePx + spacingPx) + cellSizePx / 2f
            val y = row * (cellSizePx + spacingPx) + cellSizePx / 2f
            return Offset(x, y)
        }

        fun getSlotTopLeft(index: Int): Offset {
            val row = index / 2
            val col = index % 2
            val x = col * (cellSizePx + spacingPx)
            val y = row * (cellSizePx + spacingPx)
            return Offset(x, y)
        }

        val rows = 2
        val cols = 2

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    for (col in 0 until cols) {
                        val index = row * cols + col
                        val actualSlot = photos.getOrNull(index)
                        val previewSlot = previewPhotos.getOrNull(index)
                        val hasActualPhoto = actualSlot?.uri != null || actualSlot?.url != null
                        val hasPreviewPhoto = previewSlot?.uri != null || previewSlot?.url != null
                        val isTargetHover = draggedIndex != null && targetHoverIndex == index && targetHoverIndex != draggedIndex

                        val animatedScale by animateFloatAsState(
                            targetValue = if (isTargetHover) 1.03f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            label = "slotScale_$index",
                        )

                        val mainDesc = stringResource(R.string.photo_slot_main)
                        val posDesc = stringResource(R.string.photo_slot_position, index + 1)
                        val emptyDesc = stringResource(R.string.photo_slot_empty_description, index + 1)
                        val moveNextText = stringResource(R.string.photo_slot_move_forward)
                        val movePrevText = stringResource(R.string.photo_slot_move_backward)

                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .graphicsLayer {
                                        scaleX = animatedScale
                                        scaleY = animatedScale
                                    }.border(
                                        width = if (isTargetHover) 2.5.dp else 1.dp,
                                        color =
                                            if (isTargetHover) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.outline
                                            },
                                        shape = RoundedCornerShape(8.dp),
                                    ).clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isTargetHover) {
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        },
                                    ).semantics {
                                        contentDescription =
                                            if (hasActualPhoto) {
                                                if (index == 0) mainDesc else posDesc
                                            } else {
                                                emptyDesc
                                            }
                                        if (hasActualPhoto) {
                                            customActions =
                                                buildList {
                                                    if (index < filledCount - 1) {
                                                        add(
                                                            CustomAccessibilityAction(moveNextText) {
                                                                currentPhotoReorder?.invoke(index, index + 1)
                                                                true
                                                            },
                                                        )
                                                    }
                                                    if (index > 0) {
                                                        add(
                                                            CustomAccessibilityAction(movePrevText) {
                                                                currentPhotoReorder?.invoke(index, index - 1)
                                                                true
                                                            },
                                                        )
                                                    }
                                                }
                                        }
                                    }.then(
                                        if (hasActualPhoto) {
                                            Modifier
                                                .pointerInput(index, filledCount) {
                                                    detectDragGesturesAfterLongPress(
                                                        onDragStart = {
                                                            draggedIndex = index
                                                            targetHoverIndex = index
                                                            dragOffset = Offset.Zero
                                                        },
                                                        onDragEnd = {
                                                            val from = draggedIndex
                                                            val to = targetHoverIndex
                                                            if (from != null && to != null && from != to) {
                                                                currentPhotoReorder?.invoke(from, to)
                                                            }
                                                            draggedIndex = null
                                                            targetHoverIndex = null
                                                            dragOffset = Offset.Zero
                                                        },
                                                        onDragCancel = {
                                                            val from = draggedIndex
                                                            val to = targetHoverIndex
                                                            if (from != null && to != null && from != to) {
                                                                currentPhotoReorder?.invoke(from, to)
                                                            }
                                                            draggedIndex = null
                                                            targetHoverIndex = null
                                                            dragOffset = Offset.Zero
                                                        },
                                                        onDrag = { change, dragAmount ->
                                                            change.consume()
                                                            dragOffset += dragAmount
                                                            val currentCenter = getSlotCenter(index) + dragOffset
                                                            val closest =
                                                                (0 until 4).minByOrNull { slotIdx ->
                                                                    (getSlotCenter(slotIdx) - currentCenter).getDistance()
                                                                } ?: index
                                                            val maxAllowed = (filledCount - 1).coerceAtLeast(0)
                                                            targetHoverIndex = closest.coerceIn(0, maxAllowed)
                                                        },
                                                    )
                                                }
                                                .pointerInput(index) {
                                                    detectTapGestures {
                                                        if (draggedIndex == null) {
                                                            currentSlotClick?.invoke(index)
                                                        }
                                                    }
                                                }
                                        } else {
                                            Modifier.clickableWithNoRipple {
                                                if (draggedIndex == null) {
                                                    currentSlotClick?.invoke(index)
                                                }
                                            }
                                        },
                                    ),
                        ) {
                            val slotToRender = if (draggedIndex != null) previewSlot else actualSlot

                            if (slotToRender?.uri != null) {
                                AsyncImage(
                                    model = slotToRender.uri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            } else if (slotToRender?.url != null) {
                                AsyncImage(
                                    model = slotToRender.url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            } else {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }

                            // Badge de posición accesible
                            if (hasPreviewPhoto || hasActualPhoto) {
                                Surface(
                                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                                    color =
                                        if (index == 0) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            Color.Black.copy(alpha = 0.65f)
                                        },
                                    modifier = Modifier.align(Alignment.TopStart),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    ) {
                                        if (index == 0) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(10.dp),
                                            )
                                        }
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color =
                                                if (index == 0) {
                                                    MaterialTheme.colorScheme.onPrimary
                                                } else {
                                                    Color.White
                                                },
                                        )
                                    }
                                }
                            }

                            // Indicador visual sobre la casilla destino durante el arrastre
                            androidx.compose.animation.AnimatedVisibility(
                                visible = isTargetHover,
                                enter = fadeIn() + scaleIn(initialScale = 0.85f),
                                exit = fadeOut() + scaleOut(targetScale = 0.85f),
                                modifier = Modifier.align(Alignment.Center),
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    shadowElevation = 6.dp,
                                    modifier = Modifier.padding(4.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(14.dp),
                                        )
                                        Text(
                                            text = "${index + 1}",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }

        // Elemento flotante que sigue el dedo al arrastrar
        val activeDragged = draggedIndex
        if (activeDragged != null) {
            val draggedSlot = photos.getOrNull(activeDragged)
            val originTopLeft = getSlotTopLeft(activeDragged)
            val currentX = originTopLeft.x + dragOffset.x
            val currentY = originTopLeft.y + dragOffset.y

            Box(
                modifier =
                    Modifier
                        .size(cellSizeDp)
                        .offset { IntOffset(currentX.roundToInt(), currentY.roundToInt()) }
                        .zIndex(20f)
                        .graphicsLayer {
                            scaleX = 1.08f
                            scaleY = 1.08f
                        }.shadow(12.dp, RoundedCornerShape(8.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp)),
            ) {
                if (draggedSlot?.uri != null) {
                    AsyncImage(
                        model = draggedSlot.uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else if (draggedSlot?.url != null) {
                    AsyncImage(
                        model = draggedSlot.url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
