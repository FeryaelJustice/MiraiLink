package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManager
import com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManagerImpl
import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkPrefs
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single { GlobalMiraiLinkPrefs(get(), get(ApplicationScope)) }
        single { GlobalMiraiLinkSession(get(), get(), get(ApplicationScope)) }
        viewModel { MainViewModel(get()) }
        // Remote Config Manager
        single<RemoteConfigManager> { RemoteConfigManagerImpl() }
    }
