package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MiraiLinkTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    readOnly: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    isError: Boolean = false,
    supportingText: String? = "",
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        isError = isError,
        label = { if (label.isNotBlank()) MiraiLinkText(text = label) },
        placeholder = placeholder,
        supportingText = {
            supportingText?.let {
                MiraiLinkText(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
    )
}
