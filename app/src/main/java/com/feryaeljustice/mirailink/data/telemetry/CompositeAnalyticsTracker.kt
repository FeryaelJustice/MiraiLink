package com.feryaeljustice.mirailink.data.telemetry

import android.util.Log
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent

class CompositeAnalyticsTracker(
    private val delegates: List<AnalyticsTracker>,
) : AnalyticsTracker {
    override fun logEvent(
        name: String,
        params: Map<String, Any?>,
    ) {
        delegates.forEach { delegate ->
            try {
                delegate.logEvent(name, params)
            } catch (t: Throwable) {
                Log.w("CompositeAnalytics", "Delegate ${delegate::class.java.simpleName} failed on logEvent($name)", t)
            }
        }
    }

    override fun logEvent(event: MiraiAnalyticsEvent) {
        delegates.forEach { delegate ->
            try {
                delegate.logEvent(event)
            } catch (t: Throwable) {
                Log.w("CompositeAnalytics", "Delegate ${delegate::class.java.simpleName} failed on logEvent(${event.eventName})", t)
            }
        }
    }

    override fun setUserId(id: String?) {
        delegates.forEach { delegate ->
            try {
                delegate.setUserId(id)
            } catch (t: Throwable) {
                Log.w("CompositeAnalytics", "Delegate ${delegate::class.java.simpleName} failed on setUserId", t)
            }
        }
    }

    override fun setUserProperty(
        name: String,
        value: String?,
    ) {
        delegates.forEach { delegate ->
            try {
                delegate.setUserProperty(name, value)
            } catch (t: Throwable) {
                Log.w("CompositeAnalytics", "Delegate ${delegate::class.java.simpleName} failed on setUserProperty($name)", t)
            }
        }
    }
}
