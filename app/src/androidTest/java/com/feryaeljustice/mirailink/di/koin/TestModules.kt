// test/java/.../di/koin/TestModules.kt
package com.feryaeljustice.mirailink.di.koin

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

// Serializer genérico para tests (sin encriptación)
private class TestJsonSerializer<T>(
    private val json: Json,
    private val serializer: kotlinx.serialization.KSerializer<T>,
    override val defaultValue: T,
) : Serializer<T> {
    override suspend fun readFrom(input: InputStream): T =
        try {
            val text = input.readBytes().decodeToString()
            if (text.isBlank()) defaultValue else json.decodeFromString(serializer, text)
        } catch (e: Exception) {
            defaultValue
        }

    override suspend fun writeTo(
        t: T,
        output: OutputStream,
    ) {
        output.write(json.encodeToString(serializer, t).encodeToByteArray())
    }
}

val dataStoreTestModule =
    module {
        // Json compartido
        single {
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        }

        // DataStore<AppPrefs> para MiraiLinkPrefs
        single<DataStore<AppPrefs>> {
            val context = androidContext()
            val uniqueFile =
                File(
                    context.cacheDir,
                    "test_app_prefs_${System.currentTimeMillis()}_${UUID.randomUUID()}.json",
                )
            uniqueFile.parentFile?.mkdirs()

            DataStoreFactory.create(
                serializer =
                    TestJsonSerializer(
                        json = get(),
                        serializer = AppPrefs.serializer(),
                        defaultValue = AppPrefs(),
                    ),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { uniqueFile },
            )
        }

        // DataStore<Session> con qualifier para SessionManager
        single<DataStore<Session>>(Qualifiers.SessionDataStore) {
            val context = androidContext()
            val uniqueFile =
                File(
                    context.cacheDir,
                    "test_session_${System.currentTimeMillis()}_${UUID.randomUUID()}.json",
                )
            uniqueFile.parentFile?.mkdirs()

            DataStoreFactory.create(
                serializer =
                    TestJsonSerializer(
                        json = get(),
                        serializer = Session.serializer(),
                        defaultValue = Session(),
                    ),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { uniqueFile },
            )
        }

        // Providers
        single { MiraiLinkPrefs(get()) }
        single { SessionManager(get(Qualifiers.SessionDataStore)) }
    }
