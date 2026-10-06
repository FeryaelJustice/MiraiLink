package com.feryaeljustice.mirailink.ui.components.media

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.feryaeljustice.mirailink.domain.model.capsule.PhotoPresentation
import com.feryaeljustice.mirailink.R

/** Coil executes this transformation off the main thread and keys each level separately. */
internal class CrystalMosaic(private val level: Int) : Transformation {
    override val cacheKey = "crystal-mosaic-v1-$level"
    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val edge = listOf(6, 12, 24, 48)[level.coerceIn(0, 3)]
        val small = Bitmap.createScaledBitmap(input, edge, (edge * input.height / input.width).coerceAtLeast(1), true)
        val result = Bitmap.createScaledBitmap(small, input.width, input.height, false)
        if(small !== input && small !== result) small.recycle()
        return result
    }
}

/** No pointer handlers here: the caller owns swipe, tap, zoom and long press. */
@Composable
fun CrystalPhoto(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    photoPresentation: PhotoPresentation? = null,
    unlockPulse: Int = 0,
    showCapsuleLabel: Boolean = false,
) {
    val context = LocalContext.current
    val veiled = photoPresentation?.veiled == true
    val level = photoPresentation?.level?.coerceIn(0, 3) ?: 0
    val levelLabel = stringResource(listOf(R.string.capsule_level_0, R.string.capsule_level_1,
        R.string.capsule_level_2, R.string.capsule_level_3)[level])
    val request = remember(model, veiled, level, context) {
        val builder = if(model is ImageRequest) model.newBuilder(context) else ImageRequest.Builder(context).data(model)
        builder.crossfade(false)
        if(veiled && Build.VERSION.SDK_INT < 31) builder.allowHardware(false).transformations(CrystalMosaic(level))
        builder.build()
    }
    val seed = remember(photoPresentation?.capsuleId) { (photoPresentation?.capsuleId ?: "sealed").hashCode() }
    val fracture = remember { androidx.compose.animation.core.Animatable(1f) }
    LaunchedEffect(unlockPulse) {
        if(unlockPulse > 0 && android.provider.Settings.Global.getFloat(context.contentResolver,
                android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0f) {
            fracture.snapTo(.35f)
            fracture.animateTo(1f, androidx.compose.animation.core.tween(280))
        }
    }
    Box(modifier.clipToBounds().then(if (veiled) Modifier.semantics { stateDescription = levelLabel } else Modifier)) {
        AsyncImage(model = request, contentDescription = contentDescription, contentScale = contentScale,
            modifier = Modifier.matchParentSize().then(if(veiled && Build.VERSION.SDK_INT >= 31)
                Modifier.blur(listOf(36, 24, 14, 7)[level].dp) else Modifier))
        if(veiled) Canvas(Modifier.matchParentSize()) {
            drawRect(Color(0xFFE1EDFF).copy(alpha = listOf(.62f, .43f, .29f, .16f)[level]))
            repeat(level) { index ->
                val x = (((seed ushr (index * 4)) and 15) + 4) / 24f * size.width
                val start = Offset(x, 0f)
                val middle = Offset(size.width * (.25f + index * .15f), size.height * .46f)
                val end = Offset(size.width - x, size.height)
                drawLine(Color(0xFF83DFFF).copy(alpha = .48f), start, middle, 3.dp.toPx())
                drawLine(Color(0xFFB690FF).copy(alpha = .65f), middle,
                    Offset(middle.x + (end.x - middle.x) * fracture.value,
                        middle.y + (end.y - middle.y) * fracture.value), 1.dp.toPx())
            }
        }
        if (veiled && showCapsuleLabel) {
            Surface(modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer) {
                Text(levelLabel, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
