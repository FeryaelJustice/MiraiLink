package com.feryaeljustice.mirailink.di.koin

import org.koin.core.qualifier.named

// Qualifiers to mimic Hilt's @Named and custom qualifier annotations
object Qualifiers {
    // Dispatchers & Scope
    val IoDispatcher = named("IoDispatcher")
    val MainDispatcher = named("MainDispatcher")
    val DefaultDispatcher = named("DefaultDispatcher")
    val ApplicationScope = named("ApplicationScope")

    // Network
    val BaseUrl = named("BaseUrl")
    val BaseApiUrl = named("BaseApiUrl")

    // DataStore
    val PrefsDataStore = named("PrefsDataStore")
    val SessionDataStore = named("SessionDataStore")
}
