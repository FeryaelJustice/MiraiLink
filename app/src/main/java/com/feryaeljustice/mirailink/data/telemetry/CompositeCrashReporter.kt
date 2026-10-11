package com.feryaeljustice.mirailink.data.telemetry

import android.util.Log
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent

class CompositeCrashReporter(
    private val delegates: List<CrashReporter>,
    private val analyticsTrackerProvider: (() -> AnalyticsTracker?)? = null,
) : CrashReporter {
    constructor(
        delegates: List<CrashReporter>,
        analyticsTracker: AnalyticsTracker?,
    ) : this(delegates, { analyticsTracker })

    override fun recordException(
        throwable: Throwable,
        reason: String?,
        screen: String?,
        metadata: Map<String, Any?>,
    ) {
        delegates.forEach { delegate ->
            try {
                delegate.recordException(throwable, reason, screen, metadata)
            } catch (t: Throwable) {
                Log.w("CompositeCrash", "Delegate ${delegate::class.java.simpleName} failed on recordException", t)
            }
        }

        try {
            analyticsTrackerProvider?.invoke()?.logEvent(
                MiraiAnalyticsEvent.SystemEvent.AppException(
                    exceptionType = throwable::class.java.simpleName.ifEmpty { "Throwable" },
                    reason = reason ?: throwable.message ?: "Unknown exception",
                    screen = screen,
                    isFatal = false,
                    metadata = metadata,
                ),
            )
        } catch (t: Throwable) {
            Log.w("CompositeCrash", "Failed to correlate exception event with Analytics", t)
        }
    }

    override fun log(message: String) {
        delegates.forEach { delegate ->
            try {
                delegate.log(message)
            } catch (t: Throwable) {
                Log.w("CompositeCrash", "Delegate ${delegate::class.java.simpleName} failed on log", t)
            }
        }
    }

    override fun setUserId(id: String?) {
        delegates.forEach { delegate ->
            try {
                delegate.setUserId(id)
            } catch (t: Throwable) {
                Log.w("CompositeCrash", "Delegate ${delegate::class.java.simpleName} failed on setUserId", t)
            }
        }
    }

    override fun setKey(
        key: String,
        value: String,
    ) {
        delegates.forEach { delegate ->
            try {
                delegate.setKey(key, value)
            } catch (t: Throwable) {
                Log.w("CompositeCrash", "Delegate ${delegate::class.java.simpleName} failed on setKey", t)
            }
        }
    }
}
