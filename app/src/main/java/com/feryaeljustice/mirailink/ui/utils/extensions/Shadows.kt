package com.feryaeljustice.mirailink.ui.utils.extensions

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    shape: Shape = RectangleShape
) = then(
    this.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                asFrameworkPaint().apply {
                    isAntiAlias = true
                    setColor(color.copy(alpha = alpha).toArgb())
                    maskFilter = if (blurRadius != 0.dp) {
                        BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
                    } else null
                }
            }

            val outline = shape.createOutline(
                size = size,
                layoutDirection = layoutDirection,
                density = this
            )

            canvas.save()
            canvas.translate(offsetX.toPx(), offsetY.toPx())
            canvas.drawOutline(outline, paint)
            canvas.restore()
        }
    }
)