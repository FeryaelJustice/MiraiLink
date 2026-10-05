package com.feryaeljustice.mirailink.ui.components.media

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.core.holo.HoloMaskCoverage
import com.feryaeljustice.mirailink.core.holo.HoloPhotoLayers
import com.feryaeljustice.mirailink.domain.model.holo.HoloRenderMode
import com.feryaeljustice.mirailink.ui.holo.HoloRenderController
import kotlinx.coroutines.delay

/** Dos capas decorativas dentro de la imagen. Semantica unica en la foto original. */
@Composable
fun HoloProfilePhoto(
    imageModel: String,
    contentDescription: String,
    controller: HoloRenderController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val active by controller.active.collectAsStateWithLifecycle()
    val presentationEnabled by controller.presentationEnabled.collectAsStateWithLifecycle()
    val tilt = controller.tilt.collectAsStateWithLifecycle()
    val generation by controller.images.generation.collectAsStateWithLifecycle()
    val initialGeneration = remember(imageModel) { generation }
    var viewport by remember { mutableStateOf(IntSize.Zero) }
    var bitmap by remember(imageModel) { mutableStateOf<Bitmap?>(null) }
    var layers by remember(imageModel, generation, viewport, density) { mutableStateOf<HoloPhotoLayers?>(null) }
    val key = remember(imageModel) { imageModel }
    val request = remember(context, imageModel) {
        ImageRequest.Builder(context).data(imageModel).size(1024).scale(Scale.FIT)
            .allowHardware(false).crossfade(true)
            .placeholder(R.drawable.logomirailink).error(R.drawable.logomirailink).build()
    }
    DisposableEffect(controller, key) {
        onDispose { controller.clearPhoto(key) }
    }
    LaunchedEffect(bitmap, active, viewport, density, generation) {
        val loaded = bitmap
        if (loaded == null || !active || viewport == IntSize.Zero || generation != initialGeneration || layers != null) {
            return@LaunchedEffect
        }
        delay(150)
        layers = controller.images.prepare(loaded, viewport.width, viewport.height, density)
    }
    val mode = when {
        !presentationEnabled -> HoloRenderMode.Static
        layers?.foreground != null -> HoloRenderMode.SegmentedParallax
        else -> HoloRenderMode.SimpleParallax
    }
    val baseScale = if (presentationEnabled && viewport != IntSize.Zero) {
        1f + 12f * density / minOf(viewport.width, viewport.height)
    } else 1f
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    Box(modifier.clipToBounds().testTag("holoPhoto_${mode.name}").onSizeChanged { viewport = it }) {
        AsyncImage(
            model = request, contentDescription = contentDescription, contentScale = ContentScale.Crop,
            onSuccess = { state ->
                bitmap = (state.result.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) controller.setPhotoReady(key)
            },
            onError = { bitmap = null; controller.clearPhoto(key) },
            modifier = Modifier.fillMaxSize().graphicsLayer {
                scaleX = baseScale
                scaleY = baseScale
                translationX = if (!active) 0f else -tilt.value.x * 6f * density
                translationY = if (!active) 0f else -tilt.value.y * 6f * density
            },
        )
        layers?.foreground?.takeIf { mode == HoloRenderMode.SegmentedParallax }?.let { foreground ->
            val image = remember(foreground) { foreground.asImageBitmap() }
            Image(image, contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().graphicsLayer {
                    scaleX = baseScale * HoloMaskCoverage.ForegroundScale
                    scaleY = baseScale * HoloMaskCoverage.ForegroundScale
                    translationX = if (!active) 0f else tilt.value.x * 3f * density
                    translationY = if (!active) 0f else tilt.value.y * 3f * density
                })
        }
        if (active) {
            Canvas(Modifier.fillMaxSize()) {
                val motion = tilt.value
                val strength = maxOf(kotlin.math.abs(motion.x), kotlin.math.abs(motion.y)) * 0.22f
                if (strength > 0.005f) {
                    drawRect(
                        Brush.linearGradient(listOf(primary.copy(alpha = strength),
                            secondary.copy(alpha = strength * 0.25f), primary.copy(alpha = 0f)),
                            start = Offset(size.width * (0.5f + motion.x * 0.5f), 0f),
                            end = Offset(size.width * (0.5f - motion.x * 0.5f), size.height)),
                        style = Stroke(1.5.dp.toPx()),
                    )
                }
            }
        }
    }
}
