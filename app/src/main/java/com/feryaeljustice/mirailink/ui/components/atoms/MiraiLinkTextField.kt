package com.feryaeljustice.mirailink.ui.components.atoms

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.input.VisualTransformation

/** Toolbar “vacía” para desactivar el menú de copiar/pegar */
private object NoopTextToolbar : TextToolbar {
    override val status: TextToolbarStatus get() = TextToolbarStatus.Hidden

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {
        // no-op
    }

    override fun hide() { // no-op
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    readOnly: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    isError: Boolean = false,
    supportingText: String? = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    clipboardEnabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val content: @Composable () -> Unit = {
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

    if (clipboardEnabled) {
        content()
    } else {
        CompositionLocalProvider(LocalTextToolbar provides NoopTextToolbar) {
            content()
        }
    }
}
