package com.feryaeljustice.mirailink.data.telemetry

import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import javax.inject.Inject

class FirebaseCrashlyticsReporter @Inject constructor(
    private val cl: com.google.firebase.crashlytics.FirebaseCrashlytics
) : CrashReporter {

    override fun recordNonFatal(throwable: Throwable) {
        cl.recordException(throwable)
    }

    override fun setUserId(id: String?) {
        cl.setUserId(id ?: "")
    }

    override fun setKey(key: String, value: String) {
        cl.setCustomKey(key, value)
    }
}