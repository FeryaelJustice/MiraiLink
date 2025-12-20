package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContentColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable (RowScope.() -> Unit),
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor.copy(alpha = 0.12f),
                disabledContentColor = disabledContentColor.copy(alpha = 0.38f),
            ),
        content = content,
    )
}
