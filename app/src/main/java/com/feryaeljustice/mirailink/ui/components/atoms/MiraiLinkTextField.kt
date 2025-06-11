package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MiraiLinkTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    maxLines: Int = 1,
    placeholder: @Composable (() -> Unit)? = null,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        label = { MiraiLinkText(text = label) },
        placeholder = placeholder
    )
}
