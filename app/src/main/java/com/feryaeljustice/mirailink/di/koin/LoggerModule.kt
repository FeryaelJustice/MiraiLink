// Author: Feryael Justice
// Date: 2024-07-31

package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.data.util.AndroidLogger
import com.feryaeljustice.mirailink.domain.util.Logger
import org.koin.dsl.module

val loggerModule = module {
    single<Logger> { AndroidLogger() }
}
