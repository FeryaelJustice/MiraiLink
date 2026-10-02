package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect

@Composable
fun RuleOfThirdsOverlay(
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0x3300E5FF),
    bracketColor: Color = Color(0xCC00E5FF),
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val x1 = w / 3f
        val x2 = (w * 2f) / 3f
        val y1 = h / 3f
        val y2 = (h * 2f) / 3f

        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)

        // Lineas verticales de tercios
        drawLine(
            color = lineColor,
            start = Offset(x1, 0f),
            end = Offset(x1, h),
            strokeWidth = 1.5f,
            pathEffect = dashedEffect,
        )
        drawLine(
            color = lineColor,
            start = Offset(x2, 0f),
            end = Offset(x2, h),
            strokeWidth = 1.5f,
            pathEffect = dashedEffect,
        )

        // Lineas horizontales de tercios
        drawLine(
            color = lineColor,
            start = Offset(0f, y1),
            end = Offset(w, y1),
            strokeWidth = 1.5f,
            pathEffect = dashedEffect,
        )
        drawLine(
            color = lineColor,
            start = Offset(0f, y2),
            end = Offset(w, y2),
            strokeWidth = 1.5f,
            pathEffect = dashedEffect,
        )

        // Brackets sci-fi en esquinas
        val bracketLen = 36f
        val bracketStroke = 3f
        val margin = 20f

        // Esquina superior izquierda
        drawLine(bracketColor, Offset(margin, margin), Offset(margin + bracketLen, margin), bracketStroke)
        drawLine(bracketColor, Offset(margin, margin), Offset(margin, margin + bracketLen), bracketStroke)

        // Esquina superior derecha
        drawLine(bracketColor, Offset(w - margin, margin), Offset(w - margin - bracketLen, margin), bracketStroke)
        drawLine(bracketColor, Offset(w - margin, margin), Offset(w - margin, margin + bracketLen), bracketStroke)

        // Esquina inferior izquierda
        drawLine(bracketColor, Offset(margin, h - margin), Offset(margin + bracketLen, h - margin), bracketStroke)
        drawLine(bracketColor, Offset(margin, h - margin), Offset(margin, h - margin - bracketLen), bracketStroke)

        // Esquina inferior derecha
        drawLine(bracketColor, Offset(w - margin, h - margin), Offset(w - margin - bracketLen, h - margin), bracketStroke)
        drawLine(bracketColor, Offset(w - margin, h - margin), Offset(w - margin, h - margin - bracketLen), bracketStroke)
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun RuleOfThirdsOverlayPreview() {
    RuleOfThirdsOverlay()
}
