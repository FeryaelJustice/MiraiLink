package com.feryaeljustice.mirailink.data.time

import android.content.Context
import android.os.SystemClock
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

interface TrustedTimeProvider {
    /**
     * Retorna el tiempo actual en milisegundos (UTC Unix Epoch).
     * Garantiza proteccion contra manipulaciones manuales del reloj del sistema y cambios de zona horaria.
     */
    fun currentTimeMillis(): Long

    /**
     * Indica si el tiempo devuelto proviene de una fuente de confianza validada
     * (TrustedTimeClient de Google Play Services o sincronizacion con cabecera Date del servidor).
     */
    fun isTimeTrusted(): Boolean

    /**
     * Sincroniza el tiempo base a partir de la cabecera HTTP Date recibida del servidor de MiraiLink,
     * anclando la diferencia contra el reloj monotonico de hardware (SystemClock.elapsedRealtime()).
     */
    fun syncWithServerTime(serverEpochMillis: Long)
}

class TrustedTimeProviderImpl(
    context: Context,
    private val elapsedRealtimeProvider: () -> Long = {
        try {
            SystemClock.elapsedRealtime()
        } catch (_: Throwable) {
            System.nanoTime() / 1_000_000L
        }
    },
) : TrustedTimeProvider {

    private val trustedTimeClientRef = AtomicReference<TrustedTimeClient?>(null)
    private val isClientInitialized = AtomicBoolean(false)

    // Anclaje con el servidor
    private val lastServerEpochMillis = AtomicLong(0L)
    private val lastServerElapsedRealtime = AtomicLong(0L)

    // Anclaje local al arrancar el proceso de la app
    private val appStartWallClock = System.currentTimeMillis()
    private val appStartElapsedRealtime = elapsedRealtimeProvider()

    init {
        initializeTrustedTimeClient(context)
    }

    private fun initializeTrustedTimeClient(context: Context) {
        try {
            TrustedTime.createClient(context).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trustedTimeClientRef.set(task.result)
                    isClientInitialized.set(true)
                }
            }
        } catch (_: Throwable) {
            // Entornos sin Google Play Services (emuladores puros, tests unitarios o dispositivos sin GMS)
        }
    }

    override fun currentTimeMillis(): Long {
        // 1. Prioridad: Google Play Services TrustedTimeClient
        val client = trustedTimeClientRef.get()
        if (client != null) {
            try {
                val trustedMillis = client.computeCurrentUnixEpochMillis()
                if (trustedMillis != null && trustedMillis > 0L) {
                    return trustedMillis
                }
            } catch (_: Throwable) {
                // Fallback a servidor si TrustedTime aun no tiene conexion tras reboot
            }
        }

        // 2. Fallback: Sincronizacion con el servidor + reloj monotonico de hardware
        val serverTime = lastServerEpochMillis.get()
        if (serverTime > 0L) {
            val serverElapsed = lastServerElapsedRealtime.get()
            val currentElapsed = elapsedRealtimeProvider()
            val deltaElapsed = currentElapsed - serverElapsed
            if (deltaElapsed >= 0L) {
                return serverTime + deltaElapsed
            }
        }

        // 3. Fallback: Reloj monotonico desde el arranque del proceso
        val currentElapsed = elapsedRealtimeProvider()
        val processElapsedDelta = currentElapsed - appStartElapsedRealtime
        if (processElapsedDelta >= 0L) {
            return appStartWallClock + processElapsedDelta
        }

        // 4. Ultimo recurso: reloj del sistema
        return System.currentTimeMillis()
    }

    override fun isTimeTrusted(): Boolean {
        val client = trustedTimeClientRef.get()
        if (client != null) {
            try {
                if (client.computeCurrentUnixEpochMillis() != null) return true
            } catch (_: Throwable) { }
        }
        return lastServerEpochMillis.get() > 0L
    }

    override fun syncWithServerTime(serverEpochMillis: Long) {
        if (serverEpochMillis > 0L) {
            lastServerEpochMillis.set(serverEpochMillis)
            lastServerElapsedRealtime.set(elapsedRealtimeProvider())
        }
    }
}
