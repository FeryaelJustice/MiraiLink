package com.feryaeljustice.mirailink.domain.telemetry

interface CrashReporter {
    fun recordNonFatal(throwable: Throwable)
    fun setUserId(id: String?)
    fun setKey(key: String, value: String)
}