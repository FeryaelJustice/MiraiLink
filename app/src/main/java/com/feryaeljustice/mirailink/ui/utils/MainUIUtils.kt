package com.feryaeljustice.mirailink.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastCoerceIn

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun clampOffset(raw: Offset, s: Float, size: IntSize): Offset {
    if (s <= 1f || size == IntSize.Zero) return Offset.Zero
    val maxX = (size.width * (s - 1f)) / 2f
    val maxY = (size.height * (s - 1f)) / 2f
    return Offset(
        x = raw.x.fastCoerceIn(-maxX, maxX),
        y = raw.y.fastCoerceIn(-maxY, maxY)
    )
}