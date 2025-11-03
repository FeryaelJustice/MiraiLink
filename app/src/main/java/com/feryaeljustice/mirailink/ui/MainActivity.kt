package com.feryaeljustice.mirailink.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var saveNotificationFCMUseCase: SaveNotificationFCMUseCase

    @Inject
    lateinit var applicationScope: CoroutineScope

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        newToken()

        setContent {
            MiraiLinkAppRoot()
        }
    }

    private fun newToken() {
        Firebase.messaging.token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.i("FCM", "El FCM token no fue generado")
                return@addOnCompleteListener
            }

            val token = it.result
            Log.i("FCM", "El FCM token es $token")

            applicationScope.launch { saveNotificationFCMUseCase(fcm = token) }
        }
    }
}
