package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkOutlinedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
    content: @Composable (() -> Unit),
) {
    OutlinedIconButton(modifier = modifier, colors = colors, onClick = onClick, content = content)
}

@Preview(showBackground = true)
@Composable
private fun MiraiLinkOutlinedIconButtonPreview() {
    MiraiLinkOutlinedIconButton(onClick = {}) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
    }
}

