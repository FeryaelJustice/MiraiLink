package com.feryaeljustice.mirailink.domain.telemetry

interface CrashReporter {
    fun recordException(
        throwable: Throwable,
        reason: String? = null,
        screen: String? = null,
        metadata: Map<String, Any?> = emptyMap(),
    )

    fun recordNonFatal(throwable: Throwable) = recordException(throwable)

    fun log(message: String)

    fun setUserId(id: String?)

    fun setKey(key: String, value: String)

    fun setCustomKey(key: String, value: String) = setKey(key, value)
}