package com.feryaeljustice.mirailink.core.holo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

interface HoloDevicePolicy {
    fun observeMotionAllowed(): Flow<Boolean>
}

/** Reevaluacion del sistema, sin persistir ni alterar sus opciones. */
class AndroidHoloDevicePolicy(private val context: Context) : HoloDevicePolicy {
    override fun observeMotionAllowed(): Flow<Boolean> = callbackFlow {
        fun refresh() {
            // Leer la opcion actual evita depender de la copia de ValueAnimator sin ventana adjunta.
            trySend(Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0f &&
                !context.getSystemService(PowerManager::class.java).isPowerSaveMode)
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) = refresh()
        }
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) = refresh()
        }
        context.registerReceiver(receiver, IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED))
        context.contentResolver.registerContentObserver(
            Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE), false, observer,
        )
        refresh()
        awaitClose {
            context.unregisterReceiver(receiver)
            context.contentResolver.unregisterContentObserver(observer)
        }
    }.distinctUntilChanged()
}
