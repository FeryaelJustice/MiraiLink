// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import com.feryaeljustice.mirailink.data.datastore.serializer.EncryptedJsonSerializer
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
object TestDataStoreModule {
    private const val TEST_PREFS_NAME = "mirailink_prefs_test"
    private const val TEST_SESSION_PREFS_NAME = "session_prefs_test"

    // un único scope para tod el proceso de tests
    private val testScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // guardamos las instancias creadas para no volver a crearlas
    @Volatile
    private var appPrefsDataStore: DataStore<AppPrefs>? = null

    @Volatile
    private var sessionDataStore: DataStore<Session>? = null

    @Provides
    @Singleton
    @PrefsDataStore
    fun providePrefsDataStore(
        @ApplicationContext context: Context,
        json: Json,
        secretKeyProvider: SecretKeyProvider,
    ): DataStore<AppPrefs> {
        // double-checked locking para no crear 2 datastores para el mismo fichero
        val existing = appPrefsDataStore
        if (existing != null) return existing

        return synchronized(this) {
            val again = appPrefsDataStore
            if (again != null) {
                again
            } else {
                val ds =
                    DataStoreFactory.create(
                        serializer =
                            EncryptedJsonSerializer(
                                json = json,
                                kSerializer = AppPrefs.serializer(),
                                default = AppPrefs(),
                                keyProvider = secretKeyProvider,
                            ),
                        scope = testScope,
                        produceFile = { context.dataStoreFile(TEST_PREFS_NAME) },
                    )
                appPrefsDataStore = ds
                ds
            }
        }
    }

    @Provides
    @Singleton
    @SessionDataStore
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
        json: Json,
        secretKeyProvider: SecretKeyProvider,
    ): DataStore<Session> {
        val existing = sessionDataStore
        if (existing != null) return existing

        return synchronized(this) {
            val again = sessionDataStore
            if (again != null) {
                again
            } else {
                val ds =
                    DataStoreFactory.create(
                        serializer =
                            EncryptedJsonSerializer(
                                json = json,
                                kSerializer = Session.serializer(),
                                default = Session(),
                                keyProvider = secretKeyProvider,
                            ),
                        scope = testScope,
                        produceFile = { context.dataStoreFile(TEST_SESSION_PREFS_NAME) },
                    )
                sessionDataStore = ds
                ds
            }
        }
    }
}
