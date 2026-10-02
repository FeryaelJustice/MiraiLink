package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    shape: Shape = ButtonDefaults.outlinedShape,
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(),
    content: @Composable (RowScope.() -> Unit),
) {
    OutlinedButton(
        modifier = modifier,
        colors = colors,
        onClick = onClick,
        shape = shape,
        border = border,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun MiraiLinkOutlinedButtonPreview() {
    MiraiLinkOutlinedButton(onClick = {}) {
        Text("Outlined Button")
    }
}

