package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun MiraiLinkText(
    text: String,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle? = FontStyle.Normal,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Visible
) {
    Text(
        text = text,
        modifier = modifier,
        fontStyle = fontStyle,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        style = style,
    )
}