package com.feryaeljustice.mirailink.ui

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.data.manager.AdMobManager
import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase
import com.feryaeljustice.mirailink.notification.createNotificationChannel
import com.feryaeljustice.mirailink.service.FcmService
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.theme.AppThemeManager
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val saveNotificationFCMUseCase: SaveNotificationFCMUseCase by inject()

    val applicationScope: CoroutineScope by inject(qualifier = ApplicationScope)

    val globalMiraiLinkSession: GlobalMiraiLinkSession by inject()

    // Desde splashscreen se activa el modo christmas
    private val appThemeManager: AppThemeManager by inject()
    private val mainViewModel: MainViewModel by viewModel()
    private val adMobManager: AdMobManager by inject()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /**
         * Production Mobiles ads safe to use with emulator only on debug
         */
        if (BuildConfig.DEBUG) {
            val testDeviceIds = listOf("emulator-5554", "8937551A56253163B1BB00727916310C")
            val configuration =
                RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)
        }

        createNotificationChannel(
            notificationManager = getSystemService(NotificationManager::class.java),
            channelId = FcmService.NOTIFICATION_CHANNEL_ID,
            channelName = FcmService.NOTIFICATION_CHANNEL_NAME,
            channelDescription = FcmService.NOTIFICATION_CHANNEL_DESCRIPTION,
        )

        // Firebase
        firebaseInitialize(context = applicationContext)
        newToken()

        // Google Ads (AdMob)
        initializeAds()

        setContent {
            val flags by mainViewModel.featureFlagFlow.collectAsStateWithLifecycle(
                initialValue = emptyMap(),
            )
            MiraiLinkAppRoot(appThemeManager = appThemeManager, flags = flags)
        }
    }

    private fun initializeAds() {
        adMobManager.initialize()
        lifecycleScope.launch {
            // First wait 10 seconds in purpose of initializing everything
            delay(10 * 1000L)
            while (isActive) {
                // Show ad only if app is in foreground
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    adMobManager.showInterstitial(this@MainActivity)
                }
                // Every 5 minutes
                delay(5 * 60 * 1000L)
            }
        }
    }

    private fun firebaseInitialize(context: Context) {
        Firebase.initialize(context)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
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
