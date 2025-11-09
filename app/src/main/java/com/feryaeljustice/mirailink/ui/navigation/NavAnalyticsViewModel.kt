package com.feryaeljustice.mirailink.ui.navigation

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NavAnalyticsViewModel(
    private val analytics: AnalyticsTracker,
) : ViewModel() {
    fun logScreen(route: String) {
        analytics.logEvent("screen_view", mapOf("screen_name" to route))
    }

    fun logDeepLink(uri: String) {
        analytics.logEvent("deeplink_opened", mapOf("uri" to uri))
    }
}
