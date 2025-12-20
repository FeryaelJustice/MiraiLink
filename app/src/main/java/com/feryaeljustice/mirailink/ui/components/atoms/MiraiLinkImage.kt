package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkImage(
    painterId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.None,
    hasBorder: Boolean = false,
    borderWidth: Dp = 2.dp,
    innerBorderColor: Color = Color.White,
    outerBorderColor: Color = Color.LightGray,
) {
    Image(
        painter = painterResource(id = painterId),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier =
            modifier.then(
                if (hasBorder) {
                    Modifier.drawBehind {
                        val strokeWidth = borderWidth.toPx()
                        val halfStroke = strokeWidth / 2
                        val width = size.width
                        val height = size.height

                        drawRect(
                            color = innerBorderColor,
                            topLeft = Offset(halfStroke, halfStroke),
                            size = Size(width - strokeWidth, height - strokeWidth),
                            style = Stroke(width = strokeWidth),
                        )

                        drawRect(
                            color = outerBorderColor,
                            topLeft = Offset.Zero,
                            size = Size(width, height),
                            style = Stroke(width = strokeWidth),
                        )
                    }
                } else {
                    Modifier
                },
            ),
    )
}
