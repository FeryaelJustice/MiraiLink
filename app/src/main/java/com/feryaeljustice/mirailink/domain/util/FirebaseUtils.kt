package com.feryaeljustice.mirailink.domain.util

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun applyTelemetryConsent(context: Context, consentGiven: Boolean) {
    // Analytics
    FirebaseAnalytics
        .getInstance(context)
        .setAnalyticsCollectionEnabled(consentGiven)

    // Crashlytics
    FirebaseCrashlytics
        .getInstance().isCrashlyticsCollectionEnabled = consentGiven
}
