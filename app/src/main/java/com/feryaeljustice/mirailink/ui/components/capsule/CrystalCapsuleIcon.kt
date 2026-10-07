package com.feryaeljustice.mirailink.ui.components.capsule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton

/**
 * Icono interactivo de Cápsula de Cristal para el TopBar.
 * Inicia visualmente vacío (0/4) y se llena verticalmente hasta completarse (4/4).
 */
@Composable
fun CrystalCapsuleIcon(
    modifier: Modifier = Modifier,
    progress: Int,
    isRevealed: Boolean,
    hasPendingToAnswer: Boolean = false,
    onClick: () -> Unit,
) {
    val targetFill = if (isRevealed) 1f else (progress.coerceIn(0, 4) / 4f)
    val animatedFill by animateFloatAsState(
        targetValue = targetFill,
        animationSpec = tween(durationMillis = 400),
        label = "capsule_fill",
    )

    val outlineColor = MaterialTheme.colorScheme.primary
    val fillColors = if (isRevealed) {
        listOf(Color(0xFF83DFFF), Color(0xFFB690FF), Color(0xFFFF94B8))
    } else {
        listOf(Color(0xFF64B5F6), Color(0xFFBA68C8))
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        MiraiLinkIconButton(
            onClick = onClick,
            modifier = Modifier.size(44.dp),
        ) {
            Canvas(modifier = Modifier.size(26.dp)) {
                val w = size.width
                val h = size.height

                // Silueta de gema / cápsula cristalina
                val crystalPath = Path().apply {
                    moveTo(w * 0.5f, 0f) // Punta superior
                    lineTo(w * 0.95f, h * 0.28f) // Esquina superior derecha
                    lineTo(w * 0.85f, h * 0.82f) // Lateral inferior derecho
                    lineTo(w * 0.5f, h) // Punta inferior
                    lineTo(w * 0.15f, h * 0.82f) // Lateral inferior izquierdo
                    lineTo(w * 0.05f, h * 0.28f) // Esquina superior izquierda
                    close()
                }

                // Relleno progresivo desde abajo hacia arriba
                if (animatedFill > 0f) {
                    clipPath(crystalPath) {
                        val fillHeight = h * animatedFill
                        val topY = h - fillHeight
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = fillColors,
                                startY = topY,
                                endY = h,
                            ),
                            topLeft = Offset(0f, topY),
                            size = Size(w, fillHeight),
                        )
                    }
                }

                // Borde de la gema cristalina
                drawPath(
                    path = crystalPath,
                    color = outlineColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )

                // Faceta interna sutil si está vacía
                if (animatedFill < 1f) {
                    drawLine(
                        color = outlineColor.copy(alpha = 0.35f),
                        start = Offset(w * 0.5f, h * 0.15f),
                        end = Offset(w * 0.5f, h * 0.85f),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
            }
        }

        // Indicador de turno pendiente si la otra persona propuso una pregunta
        if (hasPendingToAnswer) {
            Canvas(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 4.dp),
            ) {
                drawCircle(color = Color(0xFFFF5252))
            }
        }
    }
}
