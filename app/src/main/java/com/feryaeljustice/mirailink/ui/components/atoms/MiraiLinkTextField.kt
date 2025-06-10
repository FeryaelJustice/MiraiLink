package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MiraiLinkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder
    )
}