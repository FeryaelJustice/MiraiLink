package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Wrapper para el contenido principal de una pantalla que anima la transicion
 * entre el estado de carga y el contenido real.
 *
 * Cuando [isLoading] es true, el contenido desaparece con fade+scale y aparece
 * un [CircularProgressIndicator] centrado que ocupa el mismo espacio.
 * La transicion usa las convenciones de animacion de Material 3.
 *
 * @param isLoading Si es true, muestra el spinner en lugar del contenido.
 * @param modifier Modifier aplicado al contenedor raiz.
 * @param content El contenido de la pantalla a mostrar cuando no esta cargando.
 */
@Composable
fun MiraiLinkScreenContent(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = isLoading,
        modifier = modifier,
        transitionSpec = {
            (fadeIn() + scaleIn(initialScale = 0.92f))
                .togetherWith(fadeOut() + scaleOut(targetScale = 0.92f))
        },
        label = "ScreenContentTransition",
    ) { loading ->
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            content()
        }
    }
}
