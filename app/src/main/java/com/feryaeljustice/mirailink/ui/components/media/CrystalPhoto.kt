package com.feryaeljustice.mirailink.ui.components.media

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
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
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.capsule.PhotoPresentation

/** Coil executes this transformation off the main thread and keys each level separately. */
internal class CrystalMosaic(private val level: Int) : Transformation {
    override val cacheKey = "crystal-mosaic-v2-$level"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val edge = listOf(6, 14, 28, 56, 128)[level.coerceIn(0, 4)]
        val small = Bitmap.createScaledBitmap(input, edge, (edge * input.height / input.width).coerceAtLeast(1), true)
        val result = Bitmap.createScaledBitmap(small, input.width, input.height, false)
        if (small !== input && small !== result) small.recycle()
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
    val level = photoPresentation?.level?.coerceIn(0, 4) ?: 0
    val titles = listOf(
        R.string.capsule_level_0,
        R.string.capsule_level_1,
        R.string.capsule_level_2,
        R.string.capsule_level_3,
        R.string.capsule_level_4,
    )
    val levelLabel = stringResource(titles[level])

    val request = remember(model, veiled, level, context) {
        val builder = if (model is ImageRequest) model.newBuilder(context) else ImageRequest.Builder(context).data(model)
        builder.crossfade(false)
        if (veiled && Build.VERSION.SDK_INT < 31) {
            builder.allowHardware(false).transformations(CrystalMosaic(level))
        }
        builder.build()
    }

    Box(
        modifier = modifier
            .clipToBounds()
            .then(if (veiled) Modifier.semantics { stateDescription = levelLabel } else Modifier),
    ) {
        AsyncImage(
            model = request,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (veiled && Build.VERSION.SDK_INT >= 31) {
                        Modifier.blur(listOf(36, 26, 16, 8, 0)[level].dp)
                    } else {
                        Modifier
                    },
                ),
        )
        if (veiled) {
            Canvas(Modifier.matchParentSize()) {
                val tintAlpha = listOf(0.55f, 0.40f, 0.25f, 0.12f, 0f)[level]
                drawRect(Color(0xFFE1EDFF).copy(alpha = tintAlpha))
            }
        }
        if (veiled && showCapsuleLabel) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Text(
                    text = levelLabel,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}
