package com.feryaeljustice.mirailink.ui.navigation

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class NavAnalyticsViewModel(
    private val analytics: AnalyticsTracker,
    private val crashReporter: CrashReporter? = null,
) : ViewModel() {
    fun logScreen(route: String) {
        analytics.logEvent(MiraiAnalyticsEvent.NavigationEvent.ScreenView(route))
        crashReporter?.log("Navigated to: $route")
        crashReporter?.setCustomKey("current_screen", route)
    }

    fun logDeepLink(uri: String) {
        analytics.logEvent(MiraiAnalyticsEvent.NavigationEvent.DeepLinkOpened(uri))
        crashReporter?.log("DeepLink opened: $uri")
    }
}
