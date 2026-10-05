package com.feryaeljustice.mirailink.core.holo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import com.feryaeljustice.mirailink.domain.model.holo.HoloTilt
import com.feryaeljustice.mirailink.domain.model.holo.HoloTiltFilter
import com.feryaeljustice.mirailink.domain.model.holo.HoloRelativeRotation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlin.math.atan2
import kotlin.math.sqrt

interface HoloMotionSource {
    val available: Boolean
    fun observe(): Flow<HoloTilt>
}

/** Un listener para el propietario visible. La suscripcion cerrada libera el sensor. */
class AndroidHoloMotionSource(context: Context) : HoloMotionSource {
    private val manager = context.getSystemService(SensorManager::class.java)
    private val windows = context.getSystemService(WindowManager::class.java)
    private val sensor = listOf(
        Sensor.TYPE_GAME_ROTATION_VECTOR, Sensor.TYPE_ROTATION_VECTOR,
        Sensor.TYPE_GRAVITY, Sensor.TYPE_ACCELEROMETER,
    ).firstNotNullOfOrNull { manager.getDefaultSensor(it) }
    private var current: SensorEventListener? = null
    private var releaseOwner: (() -> Unit)? = null
    override val available get() = sensor != null

    override fun observe(): Flow<HoloTilt> = callbackFlow {
        val selected = sensor
        if (selected == null) {
            trySend(HoloTilt())
            close()
            return@callbackFlow
        }
        val filter = HoloTiltFilter()
        val matrix = FloatArray(9)
        val remapped = FloatArray(9)
        val relative = FloatArray(9)
        val orientation = HoloRelativeRotation()
        val angles = FloatArray(3)
        val gravity = FloatArray(3)
        var lastRotation = -1
        val listener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            @Suppress("DEPRECATION")
            override fun onSensorChanged(event: SensorEvent) {
                val rotation = windows.defaultDisplay.rotation
                if (lastRotation != rotation) {
                    filter.reset()
                    orientation.reset()
                    lastRotation = rotation
                    gravity.fill(0f)
                }
                val axes = when (rotation) {
                    Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
                    Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
                    Surface.ROTATION_270 -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
                    else -> SensorManager.AXIS_X to SensorManager.AXIS_Y
                }
                val tilt = if (selected.type == Sensor.TYPE_GAME_ROTATION_VECTOR ||
                    selected.type == Sensor.TYPE_ROTATION_VECTOR
                ) {
                    SensorManager.getRotationMatrixFromVector(matrix, event.values)
                    SensorManager.remapCoordinateSystem(matrix, axes.first, axes.second, remapped)
                    if (!orientation.relativeToNeutral(remapped, relative)) return
                    SensorManager.getOrientation(relative, angles)
                    filter.update(angles[1], angles[2])
                } else {
                    for (i in 0..2) gravity[i] += 0.15f * (event.values[i] - gravity[i])
                    val (gx, gy) = when (rotation) {
                        Surface.ROTATION_90 -> gravity[1] to -gravity[0]
                        Surface.ROTATION_180 -> -gravity[0] to -gravity[1]
                        Surface.ROTATION_270 -> -gravity[1] to gravity[0]
                        else -> gravity[0] to gravity[1]
                    }
                    filter.update(atan2(-gy, sqrt(gx * gx + gravity[2] * gravity[2])),
                        atan2(gx, sqrt(gy * gy + gravity[2] * gravity[2])))
                }
                trySend(tilt)
            }
        }
        synchronized(this@AndroidHoloMotionSource) {
            releaseOwner?.invoke()
            current?.let(manager::unregisterListener)
            current = listener
            releaseOwner = { close() }
            if (!manager.registerListener(listener, selected, 20_000)) close()
        }
        awaitClose {
            synchronized(this@AndroidHoloMotionSource) {
                manager.unregisterListener(listener)
                if (current === listener) {
                    current = null
                    releaseOwner = null
                }
            }
        }
    }.conflate()
}
