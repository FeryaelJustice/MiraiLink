package com.feryaeljustice.mirailink.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_DESCRIPTION
import com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_ID
import com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_NAME
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton

fun createNotificationChannel(
    notificationManager: NotificationManager,
    channelId: String = NOTIFICATION_CHANNEL_ID,
    channelName: String = NOTIFICATION_CHANNEL_NAME,
    channelImportance: Int = NotificationManager.IMPORTANCE_HIGH,
    channelDescription: String = NOTIFICATION_CHANNEL_DESCRIPTION,
) {
    val channel =
        NotificationChannel(
            channelId,
            channelName,
            channelImportance,
        )
    channel.description = channelDescription
    notificationManager.createNotificationChannel(channel)
}

@Composable
fun NotificationRationaleDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { MiraiLinkText(text = stringResource(R.string.notifications_rationale_title)) },
        text = {
            MiraiLinkText(
                text = stringResource(R.string.notifications_rationale_text),
            )
        },
        confirmButton = {
            MiraiLinkTextButton(text = stringResource(R.string.notifications_rationale_btn_confirm), onClick = onAccept)
        },
        dismissButton = {
            MiraiLinkTextButton(text = stringResource(R.string.notifications_rationale_btn_dismiss), onClick = onDismiss)
        },
    )
}
