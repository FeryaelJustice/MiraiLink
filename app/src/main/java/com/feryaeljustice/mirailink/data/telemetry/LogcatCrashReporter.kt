package com.feryaeljustice.mirailink.data.telemetry

import android.util.Log
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter

class LogcatCrashReporter(
    private val crashTag: String = "MiraiCrash",
    private val breadcrumbTag: String = "MiraiBreadcrumb",
) : CrashReporter {
    override fun recordException(
        throwable: Throwable,
        reason: String?,
        screen: String?,
        metadata: Map<String, Any?>,
    ) {
        val metaStr =
            if (metadata.isEmpty()) {
                ""
            } else {
                " metadata=" + metadata.entries.joinToString(prefix = "{", postfix = "}") { "${it.key}=${it.value}" }
            }
        val prefix = buildString {
            append("[Exception]")
            reason?.let { append(" reason='$it'") }
            screen?.let { append(" screen='$it'") }
            append(metaStr)
        }
        runCatching { Log.e(crashTag, prefix, throwable) }
    }

    override fun log(message: String) {
        runCatching { Log.v(breadcrumbTag, message) }
    }

    override fun setUserId(id: String?) {
        runCatching { Log.d(crashTag, "[UserId] $id") }
    }

    override fun setKey(
        key: String,
        value: String,
    ) {
        runCatching { Log.d(crashTag, "[CustomKey] $key=$value") }
    }
}
