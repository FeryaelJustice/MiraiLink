package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.utils.composition.LocalShowSnackbar

/** Request handled by the single SnackbarHost owned by the app root. */
data class MiraiLinkSnackbarRequest(
    val message: String,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
)

/** Shows a short-lived notification and an optional action button. */
@Composable
fun MiraiLinkSnackbar(
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(message, actionLabel) {
        showSnackbar(
            MiraiLinkSnackbarRequest(
                message = message,
                actionLabel = actionLabel,
                onAction = onAction,
            ),
        )
    }
}

/** Error-specialized wrapper with the standard localized recovery label. */
@Composable
fun MiraiLinkErrorSnackbar(
    error: UiError,
    onAction: (() -> Unit)? = null,
) {
    MiraiLinkSnackbar(
        message = error.message.asString(),
        actionLabel = error.actionLabel.asString().takeIf { onAction != null },
        onAction = onAction,
    )
}
