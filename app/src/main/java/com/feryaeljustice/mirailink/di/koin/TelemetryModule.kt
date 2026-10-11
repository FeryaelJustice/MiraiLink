package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.data.telemetry.CompositeAnalyticsTracker
import com.feryaeljustice.mirailink.data.telemetry.CompositeCrashReporter
import com.feryaeljustice.mirailink.data.telemetry.FirebaseAnalyticsTracker
import com.feryaeljustice.mirailink.data.telemetry.FirebaseCrashlyticsReporter
import com.feryaeljustice.mirailink.data.telemetry.LogcatAnalyticsTracker
import com.feryaeljustice.mirailink.data.telemetry.LogcatCrashReporter
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val telemetryModule =
    module {
        single {
            val fa = runCatching { FirebaseAnalytics.getInstance(androidContext()) }.getOrNull()
            FirebaseAnalyticsTracker(fa)
        }
        single { LogcatAnalyticsTracker() }

        single<AnalyticsTracker> {
            val delegates = mutableListOf<AnalyticsTracker>()
            delegates.add(get<FirebaseAnalyticsTracker>())
            if (BuildConfig.DEBUG) {
                delegates.add(get<LogcatAnalyticsTracker>())
            }
            CompositeAnalyticsTracker(delegates)
        }

        single {
            val cl = runCatching { FirebaseCrashlytics.getInstance() }.getOrNull()
            FirebaseCrashlyticsReporter(cl)
        }
        single { LogcatCrashReporter() }

        single<CrashReporter> {
            val delegates = mutableListOf<CrashReporter>()
            delegates.add(get<FirebaseCrashlyticsReporter>())
            if (BuildConfig.DEBUG) {
                delegates.add(get<LogcatCrashReporter>())
            }
            CompositeCrashReporter(
                delegates = delegates,
                analyticsTrackerProvider = { get<AnalyticsTracker>() },
            )
        }
    }
