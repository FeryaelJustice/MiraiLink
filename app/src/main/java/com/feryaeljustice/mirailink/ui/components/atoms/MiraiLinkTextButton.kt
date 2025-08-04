package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MiraiLinkTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isTransparentBackground: Boolean = true,
    onTransparentBackgroundContentColor: Color = MaterialTheme.colorScheme.onBackground,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContentColor: Color = MaterialTheme.colorScheme.primary
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (isTransparentBackground) Color.Transparent else containerColor,
            contentColor = if (isTransparentBackground) onTransparentBackgroundContentColor else contentColor,
            disabledContainerColor = if (isTransparentBackground) Color.Transparent else disabledContainerColor.copy(
                alpha = 0.12f
            ),
            disabledContentColor = if (isTransparentBackground) Color.Transparent else disabledContentColor.copy(
                alpha = 0.38f
            )
        )
    ) {
        MiraiLinkText(
            text = text,
            color = if (isTransparentBackground) onTransparentBackgroundContentColor else contentColor,
        )
    }
}