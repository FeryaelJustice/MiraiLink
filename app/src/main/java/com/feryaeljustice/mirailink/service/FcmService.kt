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
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class FcmService :
    FirebaseMessagingService(),
    KoinComponent {
    private val saveNotificationFCMUseCase: SaveNotificationFCMUseCase by inject()
    private val getSubscriptionStatusUseCase: com.feryaeljustice.mirailink.domain.usecase.subscription.GetSubscriptionStatusUseCase by inject()
    private val applicationScope: CoroutineScope by inject()
    private val globalMiraiLinkSession: GlobalMiraiLinkSession by inject()

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
        const val NOTIFICATION_CHANNEL_NAME = "FCM notification channel"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Channel for FCM notifications"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val type = message.data["type"]
        if (type == "subscription_updated") {
            Log.i("FCM", "Received subscription_updated push event, refreshing subscription status")
            applicationScope.launch {
                getSubscriptionStatusUseCase()
            }
            return
        }
        if (type?.startsWith("affinity_") == true) {
            val body = when (type) {
                "affinity_like" -> R.string.affinity_notification_like
                "affinity_request" -> R.string.affinity_notification_request
                "affinity_accepted" -> R.string.affinity_accepted
                "affinity_available" -> R.string.affinity_notification_available
                else -> return
            }
            showNotification(message.data["resourceId"] ?: type, getString(R.string.affinity_title), getString(body),
                destination = if (type == "affinity_like" || type == "affinity_available") "mirailink://affinities" else "mirailink://requests")
        } else showChatNotification(message = message)
    }

    /**
     * Intenta registrar la rotación del token cuando existe sesión, esperando hasta 1,5 s.
     * Sin autenticación no hay cola persistente implementada: el else solo contiene una propuesta.
     */
    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        applicationScope.launch {
            // 1) Snapshot inmediato del StateFlow
            val snapshot = globalMiraiLinkSession.currentAuth()

            // 2) Si no está autenticado, espera como mucho 1.5s a que el Flow emita TRUE
            val authed =
                if (snapshot) {
                    true
                } else {
                    withTimeoutOrNull(1_500.milliseconds) {
                        globalMiraiLinkSession.isAuthenticated.first { it }
                    } ?: false
                }

            if (authed) {
                saveNotificationFCMUseCase(fcm = token)
            } else {
                // opcional: guardar el token en local para enviarlo cuando haya login al back
                // pendingRepo.save(token)
            }
        }
    }

    /**
     * Interpreta únicamente data.type=new_message y sus campos de preview. El PendingIntent
     * abre MainActivity sin incluir conversationId como destino; no abre el chat concreto.
     */
    private fun showChatNotification(message: RemoteMessage) {
        val data = message.data

        if (data["type"] in setOf("new_message", "chat_message")) {
            val convId = data["conversationId"] ?: data["chatId"]
            val senderName = data["senderName"]
            val preview = data["messagePreview"] ?: data["text"]

            showNotification(
                messageId = convId ?: (message.messageId ?: Random.nextInt(0, 1000).toString()),
                messageTitle = senderName ?: message.notification?.title,
                messageBody = preview ?: message.notification?.body,
                destination = data["fromUserId"]?.let { "mirailink://chat/$it" },
            )
        }
    }

    private fun showNotification(
        messageId: String,
        messageTitle: String?,
        messageBody: String?,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        icon: Int = R.drawable.logomirailink,
        destination: String? = null,
    ) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val intent =
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                data = destination?.let(android.net.Uri::parse)
            }
        val pendingIntent = PendingIntent.getActivity(this, messageId.hashCode(), intent, FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val msgId = messageId.hashCode()

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
            channelId = NOTIFICATION_CHANNEL_ID,
            channelName = NOTIFICATION_CHANNEL_NAME,
            channelDescription = NOTIFICATION_CHANNEL_DESCRIPTION,
        )

        notificationManager.notify(
            msgId,
            notification,
        )
    }
}
