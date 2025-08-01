package com.feryaeljustice.mirailink.domain.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

fun generateQrImageBitmap(content: String, size: Int = 512): ImageBitmap {
    val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = createBitmap(size, size)
    for (x in 0 until size) {
        for (y in 0 until size) {
            val color =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            bitmap[x, y] = color
        }
    }
    return bitmap.asImageBitmap()
}

@Composable
fun QrCodeImage(
    imageBitmap: ImageBitmap,
    modifier: Modifier = Modifier
) {
    Image(
        bitmap = imageBitmap,
        contentDescription = "QR Code",
        modifier = modifier
    )
}