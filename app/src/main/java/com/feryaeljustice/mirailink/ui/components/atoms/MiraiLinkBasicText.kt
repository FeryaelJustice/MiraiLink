package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkBasicText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    overflow: TextOverflow = TextOverflow.Visible,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    color: Color? = null,
    autoSizeEnabled: Boolean = false,
    autoSizeMin: TextUnit = 12.sp,
    autoSizeMax: TextUnit = 112.sp,
    autoSizeStep: TextUnit = 0.25.sp,
) {
    val autoSize =
        if (autoSizeEnabled) {
            TextAutoSize.StepBased(
                minFontSize = autoSizeMin,
                maxFontSize = autoSizeMax,
                stepSize = autoSizeStep,
            )
        } else {
            null
        }

    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle,
        onTextLayout = onTextLayout,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        color = color?.let { color -> { color } },
        autoSize = autoSize,
    )
}
