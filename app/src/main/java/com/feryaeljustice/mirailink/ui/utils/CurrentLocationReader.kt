package com.feryaeljustice.mirailink.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationListener
import android.os.Build
import android.os.Looper
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

fun Context.hasForegroundLocationPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

suspend fun Context.readBestCurrentLocation(): Location? {
    if (!hasForegroundLocationPermission()) return null
    val manager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
    val providers = buildList {
        if (
            ContextCompat.checkSelfPermission(this@readBestCurrentLocation, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) add(LocationManager.GPS_PROVIDER)
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) add(LocationManager.NETWORK_PROVIDER)
    }.distinct()
    val lastKnown = try {
        providers.mapNotNull(manager::getLastKnownLocation).maxByOrNull { it.time }
    } catch (_: SecurityException) {
        null
    }
    val provider = providers.firstOrNull() ?: return lastKnown
    return try {
        suspendCancellableCoroutine { continuation ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                manager.getCurrentLocation(provider, null, ContextCompat.getMainExecutor(this)) { location ->
                    if (continuation.isActive) continuation.resume(location ?: lastKnown)
                }
            } else {
                val listener = LocationListener { location ->
                    if (continuation.isActive) continuation.resume(location)
                }
                manager.requestSingleUpdate(provider, listener, Looper.getMainLooper())
                continuation.invokeOnCancellation { manager.removeUpdates(listener) }
            }
        }
    } catch (_: SecurityException) {
        lastKnown
    }
}
