package com.feryaeljustice.mirailink.data.telemetry

import android.util.Log
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker

class LogcatAnalyticsTracker(
    private val tag: String = "MiraiAnalytics",
) : AnalyticsTracker {
    override fun logEvent(
        name: String,
        params: Map<String, Any?>,
    ) {
        val formattedParams =
            if (params.isEmpty()) {
                "{}"
            } else {
                params.entries.joinToString(prefix = "{", postfix = "}") { "${it.key}=${it.value}" }
            }
        runCatching { Log.d(tag, "[Event] $name $formattedParams") }
    }

    override fun setUserId(id: String?) {
        runCatching { Log.d(tag, "[UserId] $id") }
    }

    override fun setUserProperty(
        name: String,
        value: String?,
    ) {
        runCatching { Log.d(tag, "[UserProperty] $name=$value") }
    }
}
