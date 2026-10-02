package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    content: @Composable (() -> Unit),
) {
    IconButton(modifier = modifier, colors = colors, onClick = onClick, content = content)
}

@Preview(showBackground = true)
@Composable
private fun MiraiLinkIconButtonPreview() {
    MiraiLinkIconButton(onClick = {}) {
        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite")
    }
}
