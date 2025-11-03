package com.feryaeljustice.mirailink

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// import io.kotzilla.sdk.analytics.koin.analytics

@HiltAndroidApp
class MiraiLinkApp : Application() {
  /*  override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MiraiLinkApp)
            analytics()
        }
    }*/
}
