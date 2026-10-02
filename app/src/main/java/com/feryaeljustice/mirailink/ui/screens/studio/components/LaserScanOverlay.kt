package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun LaserScanOverlay(
    modifier: Modifier = Modifier,
    laserColor: Color = Color(0xFF00E5FF), // Cyan Neon
    trailColor: Color = Color(0x3300E5FF),
    isScanning: Boolean = true,
) {
    if (!isScanning) return

    val infiniteTransition = rememberInfiniteTransition(label = "laser_transition")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "laser_pos",
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val y = size.height * laserPosition
        val beamHeight = 60f

        // Gradiente de estela luminosa
        val beamBrush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                trailColor,
                laserColor,
                Color.White,
                laserColor,
                trailColor,
                Color.Transparent,
            ),
            startY = y - beamHeight,
            endY = y + beamHeight,
        )

        drawRect(
            brush = beamBrush,
            topLeft = Offset(0f, y - beamHeight),
            size = androidx.compose.ui.geometry.Size(size.width, beamHeight * 2),
        )

        // Linea central intensa del laser
        drawLine(
            color = Color.White,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 2.5f,
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LaserScanOverlayPreview() {
    LaserScanOverlay()
}
