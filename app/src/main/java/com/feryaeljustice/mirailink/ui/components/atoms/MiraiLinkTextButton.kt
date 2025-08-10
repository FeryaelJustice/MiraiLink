package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    disabledContentColor: Color = MaterialTheme.colorScheme.primary,
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    val currentOnClick by rememberUpdatedState(onClick)
    val currentOnLongClick by rememberUpdatedState(onLongClick)

    val backgroundColor = if (isTransparentBackground) Color.Transparent else containerColor
    val textColor = if (isTransparentBackground) {
        if (enabled) onTransparentBackgroundContentColor else onTransparentBackgroundContentColor.copy(
            alpha = 0.38f
        )
    } else {
        if (enabled) contentColor else disabledContentColor.copy(alpha = 0.38f)
    }

    val colors = ButtonDefaults.textButtonColors(
        containerColor = backgroundColor,
        contentColor = textColor,
        disabledContainerColor = if (isTransparentBackground) Color.Transparent else disabledContainerColor.copy(
            alpha = 0.12f
        ),
        disabledContentColor = if (isTransparentBackground) Color.Transparent else disabledContentColor.copy(
            alpha = 0.38f
        )
    )

    if (onLongClick != null) {
        Surface(
            modifier = modifier
                .combinedClickable(
                    onClick = currentOnClick,
                    onLongClick = currentOnLongClick
                )
                .background(backgroundColor)
                .padding(8.dp),
            color = Color.Transparent
        ) {
            MiraiLinkText(
                text = text,
                color = if (isTransparentBackground) onTransparentBackgroundContentColor else contentColor,
            )
        }
    } else {
        TextButton(
            modifier = modifier,
            onClick = currentOnClick,
            colors = colors
        ) {
            MiraiLinkText(
                text = text,
                color = textColor,
            )
        }
    }
}