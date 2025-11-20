// Author: Feryael Justice
// Date: 2024-08-02

package com.feryaeljustice.mirailink.di.koin

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class KoinTestRule(
    private val modules: List<Module>,
) : TestWatcher() {
    override fun starting(description: Description) {
        // Siempre detener Koin si existe para empezar limpio
        stopKoin()

        // Iniciar Koin con configuración de test
        startKoin {
            androidLogger(Level.ERROR) // Solo errores en tests
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
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
                dataStoreModule,
                dispatchersModule,
                loggerModule,
                networkModule,
                repositoryModule,
                serializationModule,
                socketModule,
                telemetryModule,
                useCaseModule,
                viewModelModule,
            ) + additionalModules,
    )
