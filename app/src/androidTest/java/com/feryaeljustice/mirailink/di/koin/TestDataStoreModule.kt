// Author: Feryael Justice
// Date: 2024-08-02

package com.feryaeljustice.mirailink.di.koin

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import com.feryaeljustice.mirailink.data.datastore.serializer.EncryptedJsonSerializer
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import com.feryaeljustice.mirailink.di.koin.Qualifiers.PrefsDataStore
import com.feryaeljustice.mirailink.di.koin.Qualifiers.SessionDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val TEST_PREFS_NAME = "mirailink_prefs_test"
private const val TEST_SESSION_PREFS_NAME = "session_prefs_test"

val testDataStoreModule = module {
    // Use a single CoroutineScope for all test DataStores
    single(createdAtStart = true) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single(PrefsDataStore) {
        provideTestPrefsDataStore(androidContext(), get(), get(), get())
    }

    single(SessionDataStore) {
        provideTestSessionDataStore(androidContext(), get(), get(), get())
    }
}

private fun provideTestPrefsDataStore(
    context: Context,
    json: Json,
    secretKeyProvider: SecretKeyProvider,
    scope: CoroutineScope
): DataStore<AppPrefs> {
    return DataStoreFactory.create(
        serializer = EncryptedJsonSerializer(
            json = json,
            kSerializer = AppPrefs.serializer(),
            default = AppPrefs(),
            keyProvider = secretKeyProvider
        ),
        scope = scope,
        produceFile = { context.dataStoreFile(TEST_PREFS_NAME) }
    )
}

private fun provideTestSessionDataStore(
    context: Context,
    json: Json,
    secretKeyProvider: SecretKeyProvider,
    scope: CoroutineScope
): DataStore<Session> {
    return DataStoreFactory.create(
        serializer = EncryptedJsonSerializer(
            json = json,
            kSerializer = Session.serializer(),
            default = Session(),
            keyProvider = secretKeyProvider
        ),
        scope = scope,
        produceFile = { context.dataStoreFile(TEST_SESSION_PREFS_NAME) }
    )
}
