package com.feryaeljustice.mirailink.ui

import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase
import com.feryaeljustice.mirailink.notification.createNotificationChannel
import com.feryaeljustice.mirailink.service.FcmService
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    val saveNotificationFCMUseCase: SaveNotificationFCMUseCase by inject()

    val applicationScope: CoroutineScope by inject(qualifier = ApplicationScope)

    val globalMiraiLinkSession: GlobalMiraiLinkSession by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel(
            notificationManager = getSystemService(NotificationManager::class.java),
            channelId = FcmService.NOTIFICATION_CHANNEL_ID,
            channelName = FcmService.NOTIFICATION_CHANNEL_NAME,
            channelDescription = FcmService.NOTIFICATION_CHANNEL_DESCRIPTION,
        )
        newToken()

        setContent {
            MiraiLinkAppRoot()
        }
    }

    private fun newToken() {
        Firebase.messaging.token.addOnCompleteListener { it ->
            if (!it.isSuccessful) {
                Log.i("FCM", "El FCM token no fue generado")
                return@addOnCompleteListener
            }

            val token = it.result
            Log.i("FCM", "El FCM token es $token")

            applicationScope.launch {
                // 1) Snapshot inmediato
                val snapshot = globalMiraiLinkSession.currentAuth()

                // 2) Si no está autenticado aún, espera hasta 1.5s a que emita TRUE
                val isAuthed =
                    if (snapshot) {
                        true
                    } else {
                        withTimeoutOrNull(1_500) {
                            globalMiraiLinkSession.isAuthenticated.first { it }
                        } ?: false
                    }

                if (isAuthed) {
                    saveNotificationFCMUseCase(fcm = token)
                } else {
                    // opcional: guardar para enviar tras login
                    // pendingRepo.save(token)
                }
            }
        }
    }
}
