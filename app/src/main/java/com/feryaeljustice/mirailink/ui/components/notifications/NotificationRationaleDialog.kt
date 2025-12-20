package com.feryaeljustice.mirailink.ui.components.notifications

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton

@Suppress("ktlint:standard:function-naming")
@Composable
fun NotificationRationaleDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { MiraiLinkText(text = stringResource(R.string.notifications_rationale_title)) },
        text = {
            MiraiLinkText(
                text = stringResource(R.string.notifications_rationale_text),
            )
        },
        confirmButton = {
            MiraiLinkTextButton(
                text = stringResource(R.string.notifications_rationale_btn_confirm),
                onClick = onAccept,
            )
        },
        dismissButton = {
            MiraiLinkTextButton(
                text = stringResource(R.string.notifications_rationale_btn_dismiss),
                onClick = onDismiss,
            )
        },
    )
}
