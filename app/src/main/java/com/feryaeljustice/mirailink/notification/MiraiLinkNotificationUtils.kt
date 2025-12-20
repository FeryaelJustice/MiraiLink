package com.feryaeljustice.mirailink.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_DESCRIPTION
import com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_ID
import com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_NAME

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
