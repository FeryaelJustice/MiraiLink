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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
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
    titleContent: @Composable (() -> Unit)? = null,
    messageContent: @Composable (() -> Unit)? = null,
    confirmButtonContent: @Composable (() -> Unit)? = null,
    dismissButtonContent: @Composable (() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Start
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
            titleContent?.invoke() ?: MiraiLinkText(
                text = title,
                color = textColor,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            messageContent?.invoke() ?: message?.let {
                MiraiLinkText(
                    text = it,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    textAlign = textAlign
                )
            }
        },
        confirmButton = {
            confirmButtonContent?.invoke() ?: AnimatedVisibility(showConfirmButton) {
                MiraiLinkButton(onClick = onAccept ?: {}) {
                    MiraiLinkText(
                        text = acceptText,
                        color = buttonTextColor
                    )
                }
            }
        },
        dismissButton = {
            dismissButtonContent?.invoke() ?: AnimatedVisibility(showDismissButton) {
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
