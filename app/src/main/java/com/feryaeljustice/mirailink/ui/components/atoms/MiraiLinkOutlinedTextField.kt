package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.tooling.preview.Preview

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    maxLines: Int = 1,
    isError: Boolean = false,
    supportingText: String? = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        label = { if (label.isNotBlank()) MiraiLinkText(text = label) },
        isError = isError,
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

@Preview(showBackground = true)
@Composable
private fun MiraiLinkOutlinedTextFieldPreview() {
    MiraiLinkOutlinedTextField(
        value = "Input text",
        onValueChange = {},
        label = "Label",
    )
}

