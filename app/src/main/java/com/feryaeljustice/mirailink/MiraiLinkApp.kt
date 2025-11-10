// Author: Feryael Justice
// Date: 2024-07-31

package com.feryaeljustice.mirailink

import android.app.Application
import com.feryaeljustice.mirailink.di.koin.appModule
import com.feryaeljustice.mirailink.di.koin.cryptoModule
import com.feryaeljustice.mirailink.di.koin.dataModule
import com.feryaeljustice.mirailink.di.koin.dataStoreModule
import com.feryaeljustice.mirailink.di.koin.dispatchersModule
import com.feryaeljustice.mirailink.di.koin.loggerModule
import com.feryaeljustice.mirailink.di.koin.networkModule
import com.feryaeljustice.mirailink.di.koin.repositoryModule
import com.feryaeljustice.mirailink.di.koin.socketModule
import com.feryaeljustice.mirailink.di.koin.telemetryModule
import com.feryaeljustice.mirailink.di.koin.useCaseModule
import com.feryaeljustice.mirailink.di.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration
// import io.kotzilla.sdk.analytics.koin.analytics

class MiraiLinkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MiraiLinkApp)
            androidLogger(Level.DEBUG)
//            analytics()
        }
    }

    fun initKoin(config: KoinAppDeclaration? = null) {
        startKoin {
            config?.invoke(this)
            modules(
                appModule,
                cryptoModule,
                dataModule,
                dataStoreModule,
                dispatchersModule,
                loggerModule,
                networkModule,
                repositoryModule,
                socketModule,
                telemetryModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }
}
