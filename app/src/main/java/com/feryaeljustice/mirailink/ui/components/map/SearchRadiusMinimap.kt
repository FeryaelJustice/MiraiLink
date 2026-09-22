package com.feryaeljustice.mirailink.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.cos
import kotlin.math.tan

@Composable
fun SearchRadiusMinimap(
    radiusKm: Int,
    onRadiusChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    latitude: Double = GeoUtils.DEFAULT_FALLBACK_LATITUDE,
    longitude: Double = GeoUtils.DEFAULT_FALLBACK_LONGITUDE,
    onRefreshLocation: () -> Unit = {},
    isRefreshingLocation: Boolean = false,
    minRadiusKm: Int = 10,
    maxRadiusKm: Int = 800,
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    val density = androidx.compose.ui.platform.LocalDensity.current
    val displayedTileSizePx = with(density) { 128.dp.toPx() }
    val viewportMinPx = with(density) { 210.dp.toPx() }
    val zoom = remember(radiusKm, latitude, displayedTileSizePx, viewportMinPx) {
        selectMapZoom(radiusKm.toDouble(), latitude, displayedTileSizePx, viewportMinPx)
    }
    val tilePosition = remember(latitude, longitude, zoom) {
        val worldSize = 1 shl zoom
        val normalizedX = ((longitude + 180.0) / 360.0 * worldSize)
        val latitudeRadians = Math.toRadians(latitude.coerceIn(-85.0511, 85.0511))
        val normalizedY = ((1.0 - asinh(tan(latitudeRadians)) / Math.PI) / 2.0 * worldSize)
        TilePosition(
            tileX = floor(normalizedX).toInt(),
            tileY = floor(normalizedY).toInt(),
            fractionX = normalizedX - floor(normalizedX),
            fractionY = normalizedY - floor(normalizedY),
            worldSize = worldSize,
        )
    }

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
        // 1. Mapa raster de OpenStreetMap centrado en la posicion actual.
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Un tile no se escala al ancho del mapa. Con un tamano estable, el
            // zoom y la proporcion no cambian al rotar el dispositivo.
            val tileSize = 128.dp
            val horizontalRadius = ceil(maxWidth.value / tileSize.value / 2f).toInt() + 1
            val verticalRadius = ceil(maxHeight.value / tileSize.value / 2f).toInt() + 1

            for (row in -verticalRadius..verticalRadius) {
                for (column in -horizontalRadius..horizontalRadius) {
                    val tileX = tilePosition.tileX + column
                    val tileY = (tilePosition.tileY + row)
                        .coerceIn(0, tilePosition.worldSize - 1)
                    val wrappedTileX = ((tileX % tilePosition.worldSize) + tilePosition.worldSize) % tilePosition.worldSize
                    val tileUrl = "https://tile.openstreetmap.org/$zoom/$wrappedTileX/$tileY.png"
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(tileUrl)
                            .addHeader("User-Agent", "MiraiLink-Android/1.0")
                            .crossfade(true)
                            .build(),
                        contentDescription = if (row == 0 && column == 0) {
                            stringResource(R.string.search_settings_map_preview_label)
                        } else {
                            null
                        },
                        modifier = Modifier
                            .size(tileSize)
                            .offset(
                                // La fraccion de tile de la coordenada debe caer en
                                // el centro del viewport, donde se pinta el marcador.
                                x = maxWidth / 2 + tileSize * (column - tilePosition.fractionX).toFloat(),
                                y = maxHeight / 2 + tileSize * (row - tilePosition.fractionY).toFloat(),
                            ),
                        contentScale = ContentScale.FillBounds,
                    )
                }
            }
        }

        // 2. Overlay Canvas con circulo interactivo y punto central
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height / 2f)
            val maxPixelRadius = min(size.width, size.height) * 0.44f
            val metersPerPixel = metersPerDisplayedPixel(latitude, zoom, displayedTileSizePx)
            val currentPixelRadius = (radiusKm * 1_000.0 / metersPerPixel)
                .toFloat()
                .coerceAtMost(maxPixelRadius)

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
                .align(Alignment.TopStart)
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            shadowElevation = 3.dp,
        ) {
            IconButton(
                onClick = onRefreshLocation,
                enabled = !isRefreshingLocation,
                modifier = Modifier.size(44.dp),
            ) {
                if (isRefreshingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.search_settings_refresh_location),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

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

private data class TilePosition(
    val tileX: Int,
    val tileY: Int,
    val fractionX: Double,
    val fractionY: Double,
    val worldSize: Int,
)

private const val EARTH_CIRCUMFERENCE_METERS = 40_075_016.686

internal fun metersPerDisplayedPixel(
    latitude: Double,
    zoom: Int,
    displayedTileSizePx: Float,
): Double =
    cos(Math.toRadians(latitude.coerceIn(-85.0511, 85.0511))) * EARTH_CIRCUMFERENCE_METERS /
        ((1 shl zoom) * displayedTileSizePx)

internal fun selectMapZoom(
    radiusKm: Double,
    latitude: Double,
    displayedTileSizePx: Float,
    viewportMinPx: Float,
): Int {
    val targetRadiusPx = viewportMinPx * 0.42
    return (1..18)
        .takeWhile { zoom ->
            radiusKm * 1_000.0 / metersPerDisplayedPixel(latitude, zoom, displayedTileSizePx) <= targetRadiusPx
        }
        .lastOrNull() ?: 1
}
