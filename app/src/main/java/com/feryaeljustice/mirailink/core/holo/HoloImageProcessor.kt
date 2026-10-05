package com.feryaeljustice.mirailink.core.holo

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.LruCache
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max
import kotlin.math.roundToInt

data class HoloPhotoLayers(val foreground: Bitmap?)

interface HoloImageProcessor {
    val generation: StateFlow<Long>
    suspend fun prepare(bitmap: Bitmap, widthPx: Int, heightPx: Int, density: Float): HoloPhotoLayers
    fun clear()
}

/** Cache de derivados en memoria y ejecucion serial real del SDK, incluso tras cancelacion. */
class MlKitHoloImageProcessor(
    context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val segment: suspend (Bitmap) -> HoloMask = ::segmentSingleImage,
) : HoloImageProcessor, ComponentCallbacks2 {
    private val mutex = Mutex()
    private val epoch = MutableStateFlow(0L)
    override val generation: StateFlow<Long> = epoch
    private val cache = object : LruCache<String, HoloPhotoLayers>(24 * 1024 * 1024) {
        override fun sizeOf(key: String, value: HoloPhotoLayers): Int = value.foreground?.allocationByteCount ?: 64
    }

    init { context.applicationContext.registerComponentCallbacks(this) }

    override fun clear() {
        synchronized(cache) {
            cache.evictAll()
            epoch.value += 1
        }
    }

    override suspend fun prepare(bitmap: Bitmap, widthPx: Int, heightPx: Int, density: Float): HoloPhotoLayers =
        withContext(dispatcher) {
            if (widthPx <= 0 || heightPx <= 0 || !density.isFinite() || density <= 0f) {
                return@withContext HoloPhotoLayers(null)
            }
            mutex.withLock {
                val startedEpoch = epoch.value
                // Copia propia: el ImageLoader conserva la propiedad del bitmap mostrado.
                val shrink = minOf(1f, 1024f / max(bitmap.width, bitmap.height))
                val scaled = if (shrink < 1f) Bitmap.createScaledBitmap(bitmap,
                    (bitmap.width * shrink).roundToInt().coerceAtLeast(1),
                    (bitmap.height * shrink).roundToInt().coerceAtLeast(1), true) else bitmap
                val input = try { scaled.copy(Bitmap.Config.ARGB_8888, false) } finally {
                    if (scaled !== bitmap) scaled.recycle()
                } ?: return@withLock HoloPhotoLayers(null)
                try {
                    val key = fingerprint(input, widthPx, heightPx, density)
                    synchronized(cache) { cache.get(key) }?.let { return@withLock it }
                    // Retiene el mutex hasta terminar el trabajo real, aunque desaparezca la tarjeta.
                    val mask = withContext(NonCancellable) { segment(input) }
                    val cropScale = max(widthPx.toFloat() / input.width, heightPx.toFloat() / input.height)
                    val overscan = 1f + 8f * density / minOf(widthPx, heightPx)
                    val shift = 6f * density / (cropScale * overscan)
                    val foreground = if (HoloMaskCoverage.supportsDepth(mask,
                            shift * mask.width / input.width, shift * mask.height / input.height)) {
                        createForeground(input, mask)
                    } else null
                    val result = HoloPhotoLayers(foreground)
                    synchronized(cache) {
                        if (epoch.value == startedEpoch) cache.put(key, result)
                    }
                    result
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (error: Exception) {
                    // Fallo del efecto opcional: foto original y parallax simple.
                    HoloPhotoLayers(null)
                } finally {
                    input.recycle()
                }
            }
        }

    private fun fingerprint(bitmap: Bitmap, width: Int, height: Int, density: Float): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update("holo-v1:${bitmap.width}:${bitmap.height}:$width:$height:$density".toByteArray())
        val row = IntArray(bitmap.width)
        val bytes = ByteBuffer.allocate(bitmap.width * 4)
        for (y in 0 until bitmap.height) {
            bitmap.getPixels(row, 0, bitmap.width, 0, y, bitmap.width, 1)
            bytes.clear()
            row.forEach(bytes::putInt)
            digest.update(bytes.array())
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun createForeground(bitmap: Bitmap, mask: HoloMask): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val row = IntArray(bitmap.width)
        for (y in 0 until bitmap.height) {
            bitmap.getPixels(row, 0, bitmap.width, 0, y, bitmap.width, 1)
            for (x in row.indices) {
                val value = mask.sample(x.toFloat() * (mask.width - 1) / (bitmap.width - 1).coerceAtLeast(1),
                    y.toFloat() * (mask.height - 1) / (bitmap.height - 1).coerceAtLeast(1))
                val alpha = (mask.alpha(value) * (row[x] ushr 24)).roundToInt().coerceIn(0, 255)
                row[x] = (row[x] and 0x00ffffff) or (alpha shl 24)
            }
            result.setPixels(row, 0, bitmap.width, 0, y, bitmap.width, 1)
        }
        return result
    }

    @Suppress("DEPRECATION")
    override fun onTrimMemory(level: Int) { if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) clear() }
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onLowMemory() = clear()
    override fun onConfigurationChanged(newConfig: Configuration) = Unit
}

/** Adaptador del SDK separado para poder probar concurrencia y fallos sin red ni modelo. */
private suspend fun segmentSingleImage(bitmap: Bitmap): HoloMask {
    val segmenter = Segmentation.getClient(SelfieSegmenterOptions.Builder()
        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE).enableRawSizeMask().build())
    try {
        val result = suspendCoroutine<SegmentationMask> { continuation ->
            segmenter.process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener { continuation.resume(it) }
                .addOnFailureListener { continuation.resumeWithException(it) }
                .addOnCanceledListener { continuation.resumeWithException(CancellationException()) }
        }
        val values = FloatArray(result.width * result.height)
        val buffer = result.buffer
        buffer.rewind()
        for (i in values.indices) values[i] = buffer.float
        return HoloMask(result.width, result.height, values)
    } finally { segmenter.close() }
}
