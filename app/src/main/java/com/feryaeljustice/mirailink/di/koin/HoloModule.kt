package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.core.holo.AndroidHoloDevicePolicy
import com.feryaeljustice.mirailink.core.holo.AndroidHoloMotionSource
import com.feryaeljustice.mirailink.core.holo.HoloDevicePolicy
import com.feryaeljustice.mirailink.core.holo.HoloImageProcessor
import com.feryaeljustice.mirailink.core.holo.HoloMotionSource
import com.feryaeljustice.mirailink.core.holo.MlKitHoloImageProcessor
import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.di.koin.Qualifiers.DefaultDispatcher
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.holo.HoloRenderController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val holoModule = module {
    single<HoloMotionSource> { AndroidHoloMotionSource(androidContext()) }
    single<HoloDevicePolicy> { AndroidHoloDevicePolicy(androidContext()) }
    single<HoloImageProcessor> {
        MlKitHoloImageProcessor(androidContext(), get(DefaultDispatcher)).also { images ->
            val session: GlobalMiraiLinkSession = get()
            val scope: CoroutineScope = get(ApplicationScope)
            scope.launch { session.onLogout.collect { images.clear() } }
            scope.launch {
                combine(session.currentUserId, session.isDemoMode) { id, demo -> id to demo }
                    .distinctUntilChanged().collect { images.clear() }
            }
        }
    }
    factory { HoloRenderController(get(), get(), get(), get()) }
}
