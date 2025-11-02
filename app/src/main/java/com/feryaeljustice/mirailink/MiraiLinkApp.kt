package com.feryaeljustice.mirailink

import android.app.Application
import android.app.NotificationManager
import com.feryaeljustice.mirailink.notification.createNotificationChannel
import com.feryaeljustice.mirailink.service.FcmService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MiraiLinkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(
            notificationManager = getSystemService(NotificationManager::class.java),
            channelId = FcmService.NOTIFICATION_CHANNEL_ID,
            channelName = FcmService.NOTIFICATION_CHANNEL_NAME,
            channelDescription = FcmService.NOTIFICATION_CHANNEL_DESCRIPTION,
        )
    }
}
