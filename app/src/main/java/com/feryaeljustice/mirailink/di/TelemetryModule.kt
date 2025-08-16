package com.feryaeljustice.mirailink.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TelemetryModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext ctx: Context)
            : FirebaseAnalytics =
        FirebaseAnalytics.getInstance(ctx)

    @Provides
    @Singleton
    fun provideCrashlytics()
            : FirebaseCrashlytics =
        FirebaseCrashlytics.getInstance()
}