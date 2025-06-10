package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MiraiLinkIconButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit,
    content: @Composable (() -> Unit)
) {
    IconButton(modifier = modifier, colors = colors, onClick = onClick, content = content)
}