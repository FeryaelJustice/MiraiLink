// Author: Feryael Justice
// Date: 2024-08-02

package com.feryaeljustice.mirailink.di.koin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class KoinTestRule(
    private val modules: List<Module>,
) : TestWatcher() {
    override fun starting(description: Description) {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext<Context>())
            modules(modules)
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}

fun createKoinTestRule(additionalModules: List<Module> = emptyList()) =
    KoinTestRule(
        modules =
            listOf(
                appModule,
                cryptoModule,
                dataModule,
                dispatchersModule,
                loggerModule,
                networkModule,
                repositoryModule,
                socketModule,
                telemetryModule,
                useCaseModule,
                viewModelModule,
            ) + additionalModules,
    )
