package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    content: @Composable (() -> Unit),
) {
    IconButton(modifier = modifier, colors = colors, onClick = onClick, content = content)
}
