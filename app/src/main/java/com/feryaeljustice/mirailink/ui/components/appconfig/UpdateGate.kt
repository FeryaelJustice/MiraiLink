package com.feryaeljustice.mirailink.ui.components.appconfig

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun UpdateGate(
    modifier: Modifier = Modifier,
    onOpenStore: () -> Unit,
    message: String? = stringResource(R.string.update_required_body),
    force: Boolean = true,
    onDismiss: (() -> Unit)? = {},
) {
    MiraiLinkDialog(
        modifier = modifier,
        title = stringResource(if (force) R.string.update_required_title else R.string.update_available_title),
        message = message?.ifBlank { stringResource(R.string.update_required_body) },
        properties = DialogProperties(
            dismissOnBackPress = !force,
            dismissOnClickOutside = !force,
        ),
        onDismiss = if (force) null else onDismiss,
        onAccept = onOpenStore,
        onCancel = if (force) null else onDismiss,
        showCancelButton = !force,
        acceptText = stringResource(R.string.update_now),
        cancelText = stringResource(R.string.cancel),
    )
}

@Preview(showBackground = true)
@Composable
private fun UpdateGatePreview() {
    MiraiLinkTheme {
        UpdateGate(
            onOpenStore = {},
            force = false,
        )
    }
}
