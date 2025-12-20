package com.feryaeljustice.mirailink.ui.components.appconfig

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog

@Suppress("ktlint:standard:function-naming")
@Composable
fun UpdateGate(
    onOpenStore: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = stringResource(R.string.update_required_body),
    force: Boolean = true,
    onDismiss: (() -> Unit)? = {},
) {
    MiraiLinkDialog(
        modifier = modifier,
        title = stringResource(R.string.update_required_title),
        message = message?.ifBlank { stringResource(R.string.update_required_body) },
        onDismiss =
            if (force) {
                {}
            } else {
                onDismiss
            },
        // no dejar cerrar
        onAccept = onOpenStore,
        onCancel = if (force) null else onDismiss, // sin botón cancelar
        showCancelButton = !force, // 🔒 bloqueante
        acceptText = stringResource(R.string.update_now),
        cancelText = stringResource(R.string.cancel),
    )
}
