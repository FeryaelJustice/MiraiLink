package com.feryaeljustice.mirailink.ui.components.chat.gesture

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

private data class ConfettiParticle(
    val initialX: Float,
    val initialY: Float,
    val speedX: Float,
    val speedY: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
)

@Composable
fun ConfettiCelebration(
    modifier: Modifier = Modifier,
    particleCount: Int = 65,
    onAnimationEnd: (() -> Unit)? = null,
) {
    val progress = remember { Animatable(0f) }

    val colors = listOf(
        Color(0xFFFF3366),
        Color(0xFF33CCFF),
        Color(0xFFFFCC00),
        Color(0xFF33FF99),
        Color(0xFF9933FF),
        Color(0xFFFF6600),
    )

    val particles = remember {
        val random = Random(System.currentTimeMillis())
        List(particleCount) {
            ConfettiParticle(
                initialX = random.nextFloat(),
                initialY = random.nextFloat() * 0.3f,
                speedX = (random.nextFloat() - 0.5f) * 1.5f,
                speedY = random.nextFloat() * 1.8f + 0.8f,
                size = random.nextFloat() * 14f + 8f,
                color = colors[random.nextInt(colors.size)],
                rotationSpeed = (random.nextFloat() - 0.5f) * 720f,
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2800, easing = LinearEasing),
        )
        onAnimationEnd?.invoke()
    }

    val currentProgress = progress.value

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        particles.forEach { particle ->
            val x = (particle.initialX + particle.speedX * currentProgress) * w
            val y = (particle.initialY + particle.speedY * currentProgress) * h
            val rotation = particle.rotationSpeed * currentProgress
            val alpha = (1f - currentProgress * 0.9f).coerceIn(0f, 1f)

            if (y in 0f..h && x in 0f..w) {
                rotate(degrees = rotation, pivot = Offset(x, y)) {
                    drawRect(
                        color = particle.color.copy(alpha = alpha),
                        topLeft = Offset(x - particle.size / 2, y - particle.size / 2),
                        size = Size(particle.size, particle.size * 0.6f),
                    )
                }
            }
        }
    }
}
