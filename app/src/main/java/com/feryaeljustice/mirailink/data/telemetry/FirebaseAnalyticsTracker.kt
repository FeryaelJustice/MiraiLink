package com.feryaeljustice.mirailink.data.telemetry

import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsTracker(
    private val fa: FirebaseAnalytics,
) : AnalyticsTracker {
    override fun logEvent(
        name: String,
        params: Map<String, Any?>,
    ) {
        val bundle =
            android.os.Bundle().apply {
                params.forEach { (k, v) ->
                    when (v) {
                        is String -> putString(k, v)
                        is Int -> putInt(k, v)
                        is Long -> putLong(k, v)
                        is Double -> putDouble(k, v)
                        is Float -> putFloat(k, v)
                        is Boolean -> putString(k, v.toString()) // GA4 no tiene boolean nativo
                        null -> {} // ignora
                        else -> putString(k, v.toString())
                    }
                }
            }
        fa.logEvent(name, bundle)
    }

    override fun setUserId(id: String?) {
        fa.setUserId(id)
    }

    override fun setUserProperty(
        name: String,
        value: String?,
    ) {
        fa.setUserProperty(name, value)
    }
}
