package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@Composable
fun MiraiLinkDialog(
    title: String,
    message: String? = null,
    onDismiss: () -> Unit,
    onAccept: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    acceptText: String = stringResource(R.string.accept),
    cancelText: String = stringResource(R.string.cancel),
    showAcceptButton: Boolean = true,
    showCancelButton: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    buttonTextColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val showConfirmButton by remember {
        derivedStateOf { showAcceptButton && onAccept != null }
    }
    val showDismissButton by remember {
        derivedStateOf { showCancelButton && onCancel != null }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = containerColor,
        title = {
            MiraiLinkText(
                text = title,
                color = textColor,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = if (!message.isNullOrEmpty()) {
            {
                MiraiLinkText(
                    text = message,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            null
        },
        confirmButton = {
            AnimatedVisibility(showConfirmButton) {
                MiraiLinkButton(onClick = onAccept ?: {}) {
                    MiraiLinkText(
                        text = acceptText,
                        color = buttonTextColor
                    )
                }
            }
        },
        dismissButton = {
            AnimatedVisibility(showDismissButton) {
                MiraiLinkButton(onClick = onCancel ?: {}) {
                    MiraiLinkText(
                        text = cancelText,
                        color = buttonTextColor
                    )
                }
            }
        }
    )
}
