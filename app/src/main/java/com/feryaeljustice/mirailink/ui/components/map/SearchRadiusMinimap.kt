package com.feryaeljustice.mirailink.ui.components.map

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.GeoUtils
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import kotlin.math.asinh
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.tan

@Composable
fun SearchRadiusMinimap(
    radiusKm: Int,
    onRadiusChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    latitude: Double = GeoUtils.DEFAULT_FALLBACK_LATITUDE,
    longitude: Double = GeoUtils.DEFAULT_FALLBACK_LONGITUDE,
    minRadiusKm: Int = 10,
    maxRadiusKm: Int = 300,
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    // Calculo del tile de OpenStreetMap para nivel de zoom 10
    val zoom = 10
    val tileX = remember(longitude) {
        floor((longitude + 180.0) / 360.0 * (1 shl zoom)).toInt()
    }
    val tileY = remember(latitude) {
        val latRad = Math.toRadians(latitude)
        floor((1.0 - asinh(tan(latRad)) / Math.PI) / 2.0 * (1 shl zoom)).toInt()
    }
    val tileUrl = "https://tile.openstreetmap.org/$zoom/$tileX/$tileY.png"

    // Animacion suave del radio en pantalla
    val animatedRadiusFraction by animateFloatAsState(
        targetValue = (radiusKm - minRadiusKm).toFloat() / (maxRadiusKm - minRadiusKm).toFloat(),
        animationSpec = tween(durationMillis = 150),
        label = "radiusAnim",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .background(surfaceColor)
            .pointerInput(radiusKm) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    // Arrastrar hacia arriba o derecha agranda el radio; abajo o izquierda lo reduce
                    val delta = (-dragAmount.y + dragAmount.x) / 8f
                    val newRadius = (radiusKm + delta.toInt()).coerceIn(minRadiusKm, maxRadiusKm)
                    if (newRadius != radiusKm) {
                        onRadiusChange(newRadius)
                    }
                }
            },
    ) {
        // 1. Imagen de fondo del mapa (OpenStreetMap raster tile)
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(tileUrl)
                .addHeader("User-Agent", "MiraiLink-Android/1.0")
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.search_settings_map_preview_label),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // 2. Overlay Canvas con circulo interactivo y punto central
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height / 2f)
            val maxPixelRadius = min(size.width, size.height) * 0.44f
            val minPixelRadius = min(size.width, size.height) * 0.12f
            val currentPixelRadius = minPixelRadius + (maxPixelRadius - minPixelRadius) * animatedRadiusFraction

            // Lineas de guia concentricas tenues
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = maxPixelRadius,
                center = centerOffset,
                style = Stroke(width = 1.dp.toPx()),
            )

            // Circulo de area de busqueda translúcido
            drawCircle(
                color = primaryColor.copy(alpha = 0.22f),
                radius = currentPixelRadius,
                center = centerOffset,
            )

            // Borde del circulo de radio
            drawCircle(
                color = primaryColor,
                radius = currentPixelRadius,
                center = centerOffset,
                style = Stroke(width = 2.5.dp.toPx()),
            )

            // Marcador de ubicacion del usuario en el centro
            drawCircle(
                color = primaryColor,
                radius = 8.dp.toPx(),
                center = centerOffset,
            )
            drawCircle(
                color = Color.White,
                radius = 4.dp.toPx(),
                center = centerOffset,
            )
        }

        // 3. Chip flotante con valor del radio e instruccion
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            shadowElevation = 3.dp,
        ) {
            MiraiLinkText(
                text = "$radiusKm km",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
        ) {
            MiraiLinkText(
                text = "Desliza en el mapa o ajusta la barra",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
