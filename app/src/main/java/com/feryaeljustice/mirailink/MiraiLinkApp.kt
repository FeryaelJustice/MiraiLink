package com.feryaeljustice.mirailink

import android.app.Application
import com.feryaeljustice.mirailink.di.koin.aiModule
import com.feryaeljustice.mirailink.di.koin.appModule
import com.feryaeljustice.mirailink.di.koin.cryptoModule
import com.feryaeljustice.mirailink.di.koin.dataModule
import com.feryaeljustice.mirailink.di.koin.dataStoreModule
import com.feryaeljustice.mirailink.di.koin.dispatchersModule
import com.feryaeljustice.mirailink.di.koin.featureFlagModule
import com.feryaeljustice.mirailink.di.koin.loggerModule
import com.feryaeljustice.mirailink.di.koin.networkModule
import com.feryaeljustice.mirailink.di.koin.repositoryModule
import com.feryaeljustice.mirailink.di.koin.serializationModule
import com.feryaeljustice.mirailink.di.koin.socketModule
import com.feryaeljustice.mirailink.di.koin.telemetryModule
import com.feryaeljustice.mirailink.di.koin.useCaseModule
import com.feryaeljustice.mirailink.di.koin.viewModelModule
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

class MiraiLinkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MiraiLinkApp)
            androidLogger(Level.DEBUG)
            analytics()
        }
    }

    companion object {
        // Todos los módulos
        val allModules: List<Module> =
            listOf(
                appModule,
                aiModule,
                cryptoModule,
                dataModule,
                dataStoreModule,
                dispatchersModule,
                featureFlagModule,
                loggerModule,
                networkModule,
                repositoryModule,
                serializationModule,
                socketModule,
                telemetryModule,
                useCaseModule,
                viewModelModule,
            )

        // Módulos mínimos para previews (sin red, sin telemetría, core)
        val coreModules: List<Module> =
            listOf(
                appModule,
                dataStoreModule,
                dispatchersModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )

        // Módulos de infraestructura (para tests de integración)
        val infrastructureModules: List<Module> =
            listOf(
                networkModule,
                dataModule,
                serializationModule,
            )

        val securityModules: List<Module> =
            listOf(
                cryptoModule,
            )

        fun initKoin(config: KoinAppDeclaration? = null) {
            startKoin {
                config?.invoke(this)
                modules(allModules)
            }
        }
    }
}
