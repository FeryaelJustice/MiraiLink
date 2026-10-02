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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import kotlin.math.pow

private val NeonMagenta = Color(0xFFFF2D55)
private val NeonPink = Color(0xFFFF007F)
private val NeonViolet = Color(0xFF7928CA)
private val NeonCyan = Color(0xFF00F2FE)
private val GlowReady = Color(0xFFFFD700)

@Composable
fun HapticHeartbeatOverlay(
    modifier: Modifier = Modifier,
    visible: Boolean,
    affinity: HeartbeatAffinity,
    progress: Float,
    targetNickname: String,
    onDismiss: () -> Unit,
    isSwipe: Boolean = false,
) {
    if (visible) {
        BackHandler { onDismiss() }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(180)),
        exit = fadeOut(animationSpec = tween(220)),
        modifier = modifier.zIndex(999f),
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
            targetValue = 1.25f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(cycleDurationMs / 2, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "heartBeatScale",
        )

        val isReady = progress >= 1f

        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.78f))
                    .testTag("hapticHeartbeatOverlay"),
            contentAlignment = Alignment.Center,
        ) {
            val isLandscape = maxWidth > maxHeight
            val circleDiameter =
                if (isLandscape) {
                    (maxHeight * 0.88f).coerceIn(260.dp, 330.dp)
                } else {
                    (maxWidth * 0.86f).coerceIn(310.dp, 360.dp)
                }
            val density = LocalDensity.current
            val circleRadiusPx = with(density) { (circleDiameter / 2).toPx() }

            // Ondas concentricas neon y resplandor sobre Canvas
            Canvas(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .testTag("heartbeatCanvas"),
            ) {
                val canvasCenter = Offset(size.width / 2f, size.height / 2f)
                val maxRippleRadius = circleRadiusPx * 1.55f

                // Resplandor radial suave dentro del circulo radar
                drawCircle(
                    brush =
                        Brush.radialGradient(
                            colors =
                                listOf(
                                    (if (isReady) GlowReady else NeonMagenta).copy(alpha = 0.18f),
                                    Color.Transparent,
                                ),
                            center = canvasCenter,
                            radius = circleRadiusPx,
                        ),
                    radius = circleRadiusPx,
                    center = canvasCenter,
                )

                // Renderizado de ondas concentricas expandiendose hacia afuera
                val ringCount = 3
                for (i in 0 until ringCount) {
                    val offsetPhase = (pulsePhase + (i.toFloat() / ringCount)) % 1f
                    val currentRadius =
                        circleRadiusPx + (maxRippleRadius - circleRadiusPx) * offsetPhase
                    val alpha = (1f - offsetPhase).pow(1.6f).coerceIn(0f, 1f)
                    val strokeWidth =
                        (4.dp.toPx() * (1f - (offsetPhase * 0.5f))).coerceAtLeast(1.5f)

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

                // Anillo de progreso alrededor del nucleo central
                val progressTrackTopLeft =
                    Offset(
                        canvasCenter.x - circleRadiusPx,
                        canvasCenter.y - circleRadiusPx,
                    )
                val progressTrackSize = Size(circleRadiusPx * 2f, circleRadiusPx * 2f)

                // Fondo de pista
                drawArc(
                    color = Color.White.copy(alpha = 0.15f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = progressTrackTopLeft,
                    size = progressTrackSize,
                    style = Stroke(width = 4.5.dp.toPx(), cap = StrokeCap.Round),
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
                    style = Stroke(width = 5.5.dp.toPx(), cap = StrokeCap.Round),
                )
            }

            // Nucleo Central (Icono de corazon palpitante + estadisticas e indicaciones)
            val contentMaxWidth = circleDiameter - (if (isLandscape) 36.dp else 44.dp)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier =
                    Modifier
                        .size(circleDiameter)
                        .padding(horizontal = if (isLandscape) 18.dp else 22.dp)
                        .widthIn(max = contentMaxWidth),
            ) {
                val heartBoxSize = if (isLandscape) 46.dp else 56.dp
                val heartIconSize = if (isLandscape) 26.dp else 34.dp

                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(heartBoxSize)
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
                                .size(heartIconSize)
                                .graphicsLayer {
                                    scaleX = heartBeatScale
                                    scaleY = heartBeatScale
                                },
                    )
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 2.dp else 6.dp))

                Text(
                    text = "${affinity.percentage}% Sincronía",
                    color = if (isReady) GlowReady else Color.White,
                    fontSize = if (isLandscape) 18.sp else 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 2.dp else 4.dp))

                Text(
                    text = "${affinity.bpm} BPM · Pulso Cardíaco",
                    color = NeonCyan,
                    fontSize = if (isLandscape) 11.sp else 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 2.dp else 4.dp))

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
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = if (isLandscape) 10.sp else 12.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 6.dp else 12.dp))

                val actionText =
                    when {
                        isReady -> "¡Sincronía alcanzada! Suelta para Me Gusta"
                        isSwipe -> "Desliza a la derecha para sincronizar..."
                        else -> "Mantén presionado para conectar..."
                    }

                Text(
                    text = actionText,
                    color = if (isReady) GlowReady else Color.White.copy(alpha = 0.7f),
                    fontSize = if (isLandscape) 11.sp else 12.sp,
                    fontWeight = if (isReady) FontWeight.SemiBold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HapticHeartbeatOverlayPreview() {
    MiraiLinkTheme {
        HapticHeartbeatOverlay(
            visible = true,
            affinity =
                HeartbeatAffinity(
                    ratio = 0.75f,
                    percentage = 75,
                    bpm = 85,
                    commonAnimesCount = 2,
                    commonGamesCount = 3,
                    commonGoalsCount = 1,
                ),
            progress = 0.5f,
            targetNickname = "Asuka",
            onDismiss = {},
        )
    }
}
