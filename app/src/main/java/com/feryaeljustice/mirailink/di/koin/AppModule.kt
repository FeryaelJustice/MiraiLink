package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkPrefs
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import org.koin.dsl.module

val appModule =
    module {
        single { GlobalMiraiLinkPrefs(get(), get(ApplicationScope)) }
        single { GlobalMiraiLinkSession(get(), get(), get(ApplicationScope)) }
    }
