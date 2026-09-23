package com.feryaeljustice.mirailink.ui.components.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.util.InterestImageFallback

/**
 * Tarjeta visual para representar un anime o videojuego con su portada,
 * degradado sombreado inferior de abajo hacia arriba y nombre localizado.
 *
 * @param title Nombre localizado del anime o juego.
 * @param imageUrl URL de la portada remota.
 * @param modifier Modificador de Compose.
 * @param isSelected Indica si la tarjeta esta visualmente seleccionada.
 * @param onRemoveClick Callback opcional para eliminar el item en modo edicion.
 * @param onClick Callback opcional al pulsar la tarjeta.
 */
@Composable
fun InterestCard(
    title: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onRemoveClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val fallbackDrawableRes = InterestImageFallback.getFallbackDrawableRes(context)
    val cornerShape = RoundedCornerShape(12.dp)

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl?.takeIf { it.isNotBlank() })
        .crossfade(true)
        .placeholder(fallbackDrawableRes)
        .error(fallbackDrawableRes)
        .fallback(fallbackDrawableRes)
        .build()

    Box(
        modifier = modifier
            .aspectRatio(0.72f) // Formato vertical proporcional estilo poster / card
            .clip(cornerShape)
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, cornerShape)
                } else {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), cornerShape)
                }
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
    ) {
        // Imagen de portada
        AsyncImage(
            model = imageRequest,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        // Degradado inferior sombreado de abajo hacia arriba para lectura perfecta del texto
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.35f to Color.Transparent,
                            0.70f to Color.Black.copy(alpha = 0.65f),
                            1.0f to Color.Black.copy(alpha = 0.95f),
                        )
                    )
                ),
        )

        // Nombre localizado sobreimpreso en la parte inferior
        Text(
            text = title,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
        )

        // Boton opcional de eliminar para modo edicion
        if (onRemoveClick != null) {
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(28.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
