package com.feryaeljustice.mirailink.data.studio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

object BitmapOptimizationUtils {

    suspend fun loadOptimizedBitmap(context: Context, uri: Uri): Bitmap? =
        withContext(Dispatchers.IO) {
            try {
                val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream, null, boundsOptions)
                } ?: return@withContext null

                val maxDim = max(boundsOptions.outWidth, boundsOptions.outHeight)
                var sampleSize = 1
                while (maxDim / sampleSize > 1600) {
                    sampleSize *= 2
                }

                val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
                val decoded = context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream, null, decodeOptions)
                }

                decoded?.let { bmp ->
                    val exifInput = context.contentResolver.openInputStream(uri)
                    val orientation = exifInput?.use {
                        ExifInterface(it).getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL,
                        )
                    } ?: ExifInterface.ORIENTATION_NORMAL

                    val rotation = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }

                    if (rotation != 0f) {
                        val matrix = Matrix().apply { postRotate(rotation) }
                        Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
                    } else {
                        bmp
                    }
                }
            } catch (_: Exception) {
                null
            }
        }
}
