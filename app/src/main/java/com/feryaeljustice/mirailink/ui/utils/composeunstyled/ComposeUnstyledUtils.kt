// author: Feryael Justice
// date: 26 de diciembre de 2025
package com.feryaeljustice.mirailink.ui.utils.composeunstyled

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun ArrowUp(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(8.dp, 4.dp)) {
        val path =
            Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }
        drawPath(path, color = color)
    }
}
