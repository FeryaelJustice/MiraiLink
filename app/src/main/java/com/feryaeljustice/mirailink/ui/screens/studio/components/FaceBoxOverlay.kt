package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.model.studio.NormalizedPoint
import com.feryaeljustice.mirailink.domain.model.studio.NormalizedRect
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme

@Composable
fun FaceBoxOverlay(
    modifier: Modifier = Modifier,
    faceBiometrics: FaceBiometrics?,
    isFrontCamera: Boolean,
    accentColor: Color = Color(0xFF00E5FF),
    landmarkColor: Color = Color(0xFFFF4081), // Neon Pink
) {
    if (faceBiometrics == null) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Si la camara es frontal, la imagen viene espejada horizontalmente en el preview
        fun mapX(normX: Float): Float {
            val adjusted = if (isFrontCamera) 1f - normX else normX
            return adjusted * w
        }

        val left = if (isFrontCamera) (1f - faceBiometrics.boundingBox.right) * w else faceBiometrics.boundingBox.left * w
        val top = faceBiometrics.boundingBox.top * h
        val boxWidth = faceBiometrics.boundingBox.width * w
        val boxHeight = faceBiometrics.boundingBox.height * h

        // Rectangulo suave de tracking
        drawRect(
            color = accentColor.copy(alpha = 0.25f),
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight),
            style = Stroke(width = 1.5f),
        )

        // Esquinas resaltadas estilo HUD sci-fi
        val cornerLen = (boxWidth * 0.18f).coerceIn(16f, 32f)
        val stroke = 3f

        // Arriba-Izquierda
        drawLine(accentColor, Offset(left, top), Offset(left + cornerLen, top), stroke)
        drawLine(accentColor, Offset(left, top), Offset(left, top + cornerLen), stroke)

        // Arriba-Derecha
        drawLine(accentColor, Offset(left + boxWidth, top), Offset(left + boxWidth - cornerLen, top), stroke)
        drawLine(accentColor, Offset(left + boxWidth, top), Offset(left + boxWidth, top + cornerLen), stroke)

        // Abajo-Izquierda
        drawLine(accentColor, Offset(left, top + boxHeight), Offset(left + cornerLen, top + boxHeight), stroke)
        drawLine(accentColor, Offset(left, top + boxHeight), Offset(left, top + boxHeight - cornerLen), stroke)

        // Abajo-Derecha
        drawLine(accentColor, Offset(left + boxWidth, top + boxHeight), Offset(left + boxWidth - cornerLen, top + boxHeight), stroke)
        drawLine(accentColor, Offset(left + boxWidth, top + boxHeight), Offset(left + boxWidth, top + boxHeight - cornerLen), stroke)

        // Mira central
        val centerX = left + (boxWidth / 2f)
        val centerY = top + (boxHeight / 2f)
        val reticleSize = 10f
        drawLine(accentColor, Offset(centerX - reticleSize, centerY), Offset(centerX + reticleSize, centerY), 1.5f)
        drawLine(accentColor, Offset(centerX, centerY - reticleSize), Offset(centerX, centerY + reticleSize), 1.5f)

        // Landmarks (Ojos y boca)
        faceBiometrics.leftEyePosition?.let {
            drawCircle(landmarkColor, radius = 5f, center = Offset(mapX(it.x), it.y * h))
        }
        faceBiometrics.rightEyePosition?.let {
            drawCircle(landmarkColor, radius = 5f, center = Offset(mapX(it.x), it.y * h))
        }
        faceBiometrics.noseBasePosition?.let {
            drawCircle(accentColor, radius = 4f, center = Offset(mapX(it.x), it.y * h))
        }
        faceBiometrics.mouthCenterPosition?.let {
            drawCircle(landmarkColor, radius = 4f, center = Offset(mapX(it.x), it.y * h))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FaceBoxOverlayPreview() {
    MiraiLinkTheme {
        FaceBoxOverlay(
            faceBiometrics =
                FaceBiometrics(
                    boundingBox = NormalizedRect(0.2f, 0.2f, 0.8f, 0.8f),
                    leftEyePosition = NormalizedPoint(0.35f, 0.4f),
                    rightEyePosition = NormalizedPoint(0.65f, 0.4f),
                    noseBasePosition = NormalizedPoint(0.5f, 0.55f),
                    mouthCenterPosition = NormalizedPoint(0.5f, 0.7f),
                    smilingProbability = 0.9f,
                    leftEyeOpenProbability = 0.95f,
                    rightEyeOpenProbability = 0.95f,
                    headEulerAngleX = 0f,
                    headEulerAngleY = 0f,
                    headEulerAngleZ = 0f,
                ),
            isFrontCamera = true,
        )
    }
}
