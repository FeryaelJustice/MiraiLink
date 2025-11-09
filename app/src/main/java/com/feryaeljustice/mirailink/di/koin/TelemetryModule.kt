// Author: Feryael Justice
// Date: 2024-07-31

package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.data.telemetry.FirebaseAnalyticsTracker
import com.feryaeljustice.mirailink.data.telemetry.FirebaseCrashlyticsReporter
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val telemetryModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
    single { FirebaseCrashlytics.getInstance() }

    single<AnalyticsTracker> { FirebaseAnalyticsTracker(get()) }
    single<CrashReporter> { FirebaseCrashlyticsReporter(get()) }
}
