package com.feryaeljustice.mirailink.ui.components.media

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.viewentities.PhotoSlotViewEntity

@Composable
fun EditablePhotoGrid(
    photos: List<PhotoSlotViewEntity>,
    onSlotClick: ((Int) -> Unit)?,
    onPhotoReorder: ((Int, Int) -> Unit)?
) {
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    val rows = 2
    val cols = 2

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (col in 0 until cols) {
                    val index = row * cols + col
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { draggedIndex = index },
                                    onDragEnd = { draggedIndex = null; dragOffset = Offset.Zero },
                                    onDragCancel = {
                                        draggedIndex = null; dragOffset = Offset.Zero
                                    },
                                    onDrag = { change, _ ->
                                        dragOffset = change.position
                                    }
                                )
                            }
                            .clickableWithNoRipple { onSlotClick?.invoke(index) }
                    ) {
                        val slot = photos.getOrNull(index)

                        if (slot?.uri != null) {
                            // Si hay imagen nueva desde galería/cámara (local)
                            AsyncImage(
                                model = slot.uri,
                                contentDescription = stringResource(R.string.content_description_editable_photo_grid_user_photo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (slot?.url != null) {
                            // Imagen existente del backend (remota)
                            AsyncImage(
                                model = slot.url,
                                contentDescription = stringResource(R.string.content_description_editable_photo_grid_user_photo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = stringResource(R.string.content_description_editable_photo_grid_add_photo),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.Center)
                            )
                        }

                        // Sombra cuando se está arrastrando
                        if (draggedIndex == index) {
                            Surface(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.25f))
                            ) {}
                        }
                    }
                }
            }
        }

        // Detectar reorden
        LaunchedEffect(draggedIndex, dragOffset) {
            val from = draggedIndex ?: return@LaunchedEffect
            val to = getSlotIndexFromOffset(dragOffset, gridSize = 2)
            if (to != null && to != from) {
                onPhotoReorder?.invoke(from, to)
                draggedIndex = to
            }
        }
    }
}

// Utilidad para detectar clic sin ripple
@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun Modifier.clickableWithNoRipple(onClick: () -> Unit): Modifier = composed {
    this.then(
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { onClick() })
        }
    )
}

// Cálculo de índice desde offset
private fun getSlotIndexFromOffset(offset: Offset, gridSize: Int): Int? {
    val cellSizePx = 300f // Ajustar según UI real
    val col = (offset.x / cellSizePx).toInt()
    val row = (offset.y / cellSizePx).toInt()
    val index = row * gridSize + col
    return if (index in 0..3) index else null
}
