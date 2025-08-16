package com.feryaeljustice.mirailink.domain.telemetry

interface AnalyticsTracker {
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
    fun setUserId(id: String?)
    fun setUserProperty(name: String, value: String?)
}