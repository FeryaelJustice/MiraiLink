package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.data.telemetry.FirebaseAnalyticsTracker
import com.feryaeljustice.mirailink.data.telemetry.FirebaseCrashlyticsReporter
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TelemetryBindModule {
    @Binds
    abstract fun bindAnalyticsTracker(impl: FirebaseAnalyticsTracker): AnalyticsTracker
    @Binds
    abstract fun bindCrashReporter(impl: FirebaseCrashlyticsReporter): CrashReporter
}