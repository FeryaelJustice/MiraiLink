package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun ThemeSwitcher(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    size: Dp = 48.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
) {
    val animationSpec = tween<Float>(durationMillis = 300)

    // Animamos el sesgo: -1 es izquierda (start), 1 es derecha (end)
    val alignmentBias by animateFloatAsState(
        targetValue = if (darkTheme) -1f else 1f,
        animationSpec = animationSpec,
        label = "thumbAlignment",
    )

    Box(
        modifier =
            modifier
                .width(size * 2)
                .height(size)
                .clip(parentShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .border(
                    width = borderWidth,
                    color = MaterialTheme.colorScheme.primary,
                    shape = parentShape,
                ).clickable { onClick() },
    ) {
        // Capa 1: Los Iconos (fondo)
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = R.drawable.nightlight_24px),
                    tint = if (darkTheme) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.theme_icon),
                )
            }
            Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = R.drawable.sunny_24px),
                    tint = if (darkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(R.string.theme_icon),
                )
            }
        }

        // Capa 2: El Pulgar (Círculo que se mueve)
        // CORRECCIÓN: Quitamos el fillMaxSize y aplicamos el align directamente aquí
        Box(
            modifier =
                Modifier
                    .size(size) // El tamaño del área del pulgar
                    .align(BiasAlignment(horizontalBias = alignmentBias, verticalBias = 0f)) // ¡Aquí ocurre la magia!
                    .padding(all = padding) // El padding reduce el area pintada dentro del tamaño total
                    .clip(shape = toggleShape)
                    .background(MaterialTheme.colorScheme.primary),
        )
    }
}
