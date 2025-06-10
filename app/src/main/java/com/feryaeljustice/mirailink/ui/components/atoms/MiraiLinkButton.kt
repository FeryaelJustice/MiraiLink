package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MiraiLinkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = content
    )
}