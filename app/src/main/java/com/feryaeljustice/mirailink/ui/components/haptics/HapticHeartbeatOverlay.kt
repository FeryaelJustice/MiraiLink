package com.feryaeljustice.mirailink.ui.components.haptics

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity
import kotlin.math.pow

private val NeonMagenta = Color(0xFFFF2D55)
private val NeonPink = Color(0xFFFF007F)
private val NeonViolet = Color(0xFF7928CA)
private val NeonCyan = Color(0xFF00F2FE)
private val GlowReady = Color(0xFFFFD700)

@Composable
fun HapticHeartbeatOverlay(
    visible: Boolean,
    affinity: HeartbeatAffinity,
    progress: Float,
    targetNickname: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (visible) {
        BackHandler { onDismiss() }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(180)),
        exit = fadeOut(animationSpec = tween(220)),
        modifier = modifier.zIndex(99f),
    ) {
        val cycleDurationMs = (60_000 / affinity.bpm).coerceIn(400, 1100)
        val infiniteTransition = rememberInfiniteTransition(label = "HeartbeatRipples")

        val pulsePhase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(cycleDurationMs, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "pulsePhase",
        )

        val heartBeatScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.28f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(cycleDurationMs / 2, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "heartBeatScale",
        )

        val isReady = progress >= 1f

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.72f))
                    .testTag("hapticHeartbeatOverlay"),
            contentAlignment = Alignment.Center,
        ) {
            // Ondas concentricas neón sobre Canvas
            Canvas(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .testTag("heartbeatCanvas"),
            ) {
                val canvasCenter = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = (size.minDimension / 1.7f)

                // Renderizado de 3 capas de ondas concentricas desfasadas
                val ringCount = 3
                for (i in 0 until ringCount) {
                    val offsetPhase = (pulsePhase + (i.toFloat() / ringCount)) % 1f
                    val currentRadius = 60.dp.toPx() + (maxRadius - 60.dp.toPx()) * offsetPhase
                    val alpha = (1f - offsetPhase).pow(1.6f).coerceIn(0f, 1f)

                    val strokeWidth = (6.dp.toPx() * (1f - (offsetPhase * 0.5f))).coerceAtLeast(1.5f)

                    val ringColor =
                        if (isReady) {
                            GlowReady.copy(alpha = alpha * 0.9f)
                        } else if (affinity.ratio >= 0.70f) {
                            NeonMagenta.copy(alpha = alpha * 0.85f)
                        } else if (affinity.ratio >= 0.40f) {
                            NeonViolet.copy(alpha = alpha * 0.75f)
                        } else {
                            NeonCyan.copy(alpha = alpha * 0.65f)
                        }

                    drawCircle(
                        color = ringColor,
                        radius = currentRadius,
                        center = canvasCenter,
                        style = Stroke(width = strokeWidth),
                    )
                }

                // Anillo de progreso alrededor del nucleo central (umbral de 1.2s)
                val progressTrackRadius = 88.dp.toPx()
                val progressTrackTopLeft =
                    Offset(
                        canvasCenter.x - progressTrackRadius,
                        canvasCenter.y - progressTrackRadius,
                    )
                val progressTrackSize = Size(progressTrackRadius * 2f, progressTrackRadius * 2f)

                // Fondo de pista
                drawArc(
                    color = Color.White.copy(alpha = 0.15f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = progressTrackTopLeft,
                    size = progressTrackSize,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                )

                // Arco de carga progresiva
                val sweepAngle = (progress.coerceIn(0f, 1f) * 360f)
                val progressArcBrush =
                    Brush.sweepGradient(
                        colors =
                            if (isReady) {
                                listOf(GlowReady, NeonPink, GlowReady)
                            } else {
                                listOf(NeonCyan, NeonViolet, NeonMagenta)
                            },
                        center = canvasCenter,
                    )

                drawArc(
                    brush = progressArcBrush,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = progressTrackTopLeft,
                    size = progressTrackSize,
                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round),
                )
            }

            // Nucleo Central (Icono de corazon palpitante + estadisticas)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            if (isReady) GlowReady.copy(alpha = 0.35f) else NeonMagenta.copy(alpha = 0.35f),
                                            Color.Transparent,
                                        ),
                                ),
                            ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.like),
                        tint = if (isReady) GlowReady else NeonPink,
                        modifier =
                            Modifier
                                .size(56.dp)
                                .graphicsLayer {
                                    scaleX = heartBeatScale
                                    scaleY = heartBeatScale
                                },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${affinity.percentage}% Sincronía",
                    color = if (isReady) GlowReady else Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${affinity.bpm} BPM · Pulso Cardíaco",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(4.dp))

                val interestsDetail =
                    buildString {
                        val parts = mutableListOf<String>()
                        if (affinity.commonGamesCount > 0) parts.add("${affinity.commonGamesCount} juegos")
                        if (affinity.commonAnimesCount > 0) parts.add("${affinity.commonAnimesCount} animes")
                        if (affinity.commonGoalsCount > 0) parts.add("${affinity.commonGoalsCount} metas")
                        if (parts.isNotEmpty()) {
                            append(parts.joinToString(" · "))
                            append(" en común")
                        } else {
                            append("Sincronizando con $targetNickname")
                        }
                    }

                Text(
                    text = interestsDetail,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text =
                        if (isReady) {
                            "¡Sincronía alcanzada! Suelta para Me Gusta"
                        } else {
                            "Mantén presionado para conectar..."
                        },
                    color = if (isReady) GlowReady else Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = if (isReady) FontWeight.SemiBold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
