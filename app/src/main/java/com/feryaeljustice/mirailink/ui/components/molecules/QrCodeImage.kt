package com.feryaeljustice.mirailink.ui.components.molecules

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Suppress("ktlint:standard:function-naming")
@SuppressLint("UseKtx")
@Composable
fun QrCodeImage(
    content: String,
    modifier: Modifier = Modifier,
    size: Dp = 240.dp,
) {
    val bitmap =
        remember(content) {
            val writer = QRCodeWriter()
            val matrix =
                writer.encode(content, BarcodeFormat.QR_CODE, size.value.toInt(), size.value.toInt())
            createBitmap(size.value.toInt(), size.value.toInt())
                .apply {
                    for (x in 0 until size.value.toInt()) {
                        for (y in 0 until size.value.toInt()) {
                            setPixel(x, y, if (matrix[x, y]) Color.Black.toArgb() else Color.White.toArgb())
                        }
                    }
                }.asImageBitmap()
        }
    Image(
        bitmap = bitmap,
        contentDescription = "QR Code",
        modifier = modifier,
    )
}
