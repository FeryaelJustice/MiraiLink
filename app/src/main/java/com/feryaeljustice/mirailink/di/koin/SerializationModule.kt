// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.di.koin

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val serializationModule =
    module {
        single {
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        }
    }
