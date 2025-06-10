package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MiraiLinkOutlinedIconButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
    onClick: () -> Unit,
    content: @Composable (() -> Unit)
) {
    OutlinedIconButton(modifier = modifier, colors = colors, onClick = onClick, content = content)
}