// Author: Feryael Justice
// Date: 2024-08-02

package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.di.koin.Qualifiers.DefaultDispatcher
import com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher
import com.feryaeljustice.mirailink.di.koin.Qualifiers.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dispatchersModule =
    module {
        single<CoroutineDispatcher>(IoDispatcher) { Dispatchers.IO }
        single<CoroutineDispatcher>(DefaultDispatcher) { Dispatchers.Default }
        single<CoroutineDispatcher>(MainDispatcher) { Dispatchers.Main }
        single(ApplicationScope) { CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(IoDispatcher)) }
    }
