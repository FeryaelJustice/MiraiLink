package com.feryaeljustice.mirailink.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase
import com.feryaeljustice.mirailink.notification.createNotificationChannel
import com.feryaeljustice.mirailink.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {
    @Inject
    lateinit var saveNotificationFCMUseCase: SaveNotificationFCMUseCase

    @Inject
    lateinit var applicationScope: CoroutineScope

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
        const val NOTIFICATION_CHANNEL_NAME = "FCM notification channel"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Channel for FCM notifications"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i("FCM", "Tenemos nuevo mensaje desde el FirebaseMessaginService: $message")
        showChatNotification(message = message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCM", "Tenemos nuevo token desde el FirebaseMessaginService: $token")
        applicationScope.launch {
            saveNotificationFCMUseCase(fcm = token)
        }
    }

    private fun showChatNotification(message: RemoteMessage) {
        val data = message.data

        if (data["type"] == "new_message") {
            val convId = data["conversationId"]
            val senderName = data["senderName"]
            val preview = data["messagePreview"]

            showNotification(
                messageId = convId ?: (message.messageId ?: Random.nextInt(0, 1000).toString()),
                messageTitle = senderName ?: message.notification?.title,
                messageBody = preview ?: message.notification?.body,
            )
        }
    }

    private fun showNotification(
        messageId: String,
        messageTitle: String?,
        messageBody: String?,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        icon: Int = R.drawable.logomirailink,
    ) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val intent =
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)

        val msgId = messageId.toIntOrNull() ?: Random.nextInt(0, 1000)

        val me = Person.Builder().setName(getString(R.string.you)).build()
        val sender = Person.Builder().setName(messageTitle ?: getString(R.string.contact)).build()
        val style =
            NotificationCompat.MessagingStyle(me).setConversationTitle(messageTitle).addMessage(
                messageBody ?: "",
                System.currentTimeMillis(),
                sender,
            )

        val notification =
            NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(icon)
                .setStyle(style)
                .setPriority(priority)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build()

        createNotificationChannel(
            notificationManager = getSystemService(NotificationManager::class.java),
            channelId = this.getString(R.string.default_notification_channel_id),
            channelName = NOTIFICATION_CHANNEL_NAME,
            channelDescription = NOTIFICATION_CHANNEL_DESCRIPTION,
        )

        notificationManager.notify(
            msgId,
            notification,
        )
    }
}
