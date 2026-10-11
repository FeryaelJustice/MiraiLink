package com.feryaeljustice.mirailink.data.telemetry

import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashlyticsReporter(
    private val cl: FirebaseCrashlytics?,
) : CrashReporter {
    override fun recordException(
        throwable: Throwable,
        reason: String?,
        screen: String?,
        metadata: Map<String, Any?>,
    ) {
        val reporter = cl ?: return
        reason?.let { reporter.setCustomKey("error_reason", it) }
        screen?.let { reporter.setCustomKey("screen_name", it) }
        metadata.forEach { (key, value) ->
            when (value) {
                is Boolean -> reporter.setCustomKey(key, value)
                is Double -> reporter.setCustomKey(key, value)
                is Float -> reporter.setCustomKey(key, value)
                is Int -> reporter.setCustomKey(key, value)
                is Long -> reporter.setCustomKey(key, value)
                is String -> reporter.setCustomKey(key, value)
                null -> {}
                else -> reporter.setCustomKey(key, value.toString())
            }
        }
        reporter.recordException(throwable)
    }

    override fun log(message: String) {
        cl?.log(message)
    }

    override fun setUserId(id: String?) {
        cl?.setUserId(id ?: "")
    }

    override fun setKey(
        key: String,
        value: String,
    ) {
        cl?.setCustomKey(key, value)
    }
}
