package com.feryaeljustice.mirailink.di.koin

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import com.feryaeljustice.mirailink.data.datastore.serializer.EncryptedJsonSerializer
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher
import com.feryaeljustice.mirailink.di.koin.Qualifiers.PrefsDataStore
import com.feryaeljustice.mirailink.di.koin.Qualifiers.SessionDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATASTORE_PREFS_NAME = "mirailink_prefs"
private const val DATASTORE_SESSION_PREFS_NAME = "session_prefs"

val dataStoreModule =
    module {
        single(PrefsDataStore) {
            providePrefsDataStore(androidContext(), get(), get(), get(IoDispatcher))
        }

        single(SessionDataStore) {
            provideSessionPrefsDataStore(androidContext(), get(), get(), get(IoDispatcher))
        }
    }

private fun providePrefsDataStore(
    context: Context,
    json: Json,
    keyProvider: SecretKeyProvider,
    ioDispatcher: CoroutineDispatcher,
): DataStore<AppPrefs> =
    DataStoreFactory.create(
        serializer =
            EncryptedJsonSerializer(
                json = json,
                kSerializer = AppPrefs.serializer(),
                default = AppPrefs(),
                keyProvider = keyProvider,
            ),
        scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        produceFile = { context.dataStoreFile(DATASTORE_PREFS_NAME) },
    )

private fun provideSessionPrefsDataStore(
    context: Context,
    json: Json,
    keyProvider: SecretKeyProvider,
    ioDispatcher: CoroutineDispatcher,
): DataStore<Session> {
    return DataStoreFactory.create(
        serializer =
            EncryptedJsonSerializer(
                json = json,
                kSerializer = Session.serializer(),
                default = Session(),
                keyProvider = keyProvider,
            ),
        migrations =
            listOf(
                object : DataMigration<Session> {
                    override suspend fun shouldMigrate(currentData: Session) = currentData == Session()

                    override suspend fun migrate(currentData: Session): Session {
                        val sp = context.getSharedPreferences(DATASTORE_SESSION_PREFS_NAME, Context.MODE_PRIVATE)
                        val token = sp.getString("jwt_token", "") ?: ""
                        val userId = sp.getString("user_id", "") ?: ""
                        val verified = sp.getBoolean("verified", false)
                        return if (token.isBlank() || userId.isBlank()) {
                            currentData
                        } else {
                            Session(token = token, userId = userId, verified = verified)
                        }
                    }

                    override suspend fun cleanUp() {}
                },
                object : DataMigration<Session> {
                    override suspend fun shouldMigrate(currentData: Session) = currentData == Session()

                    override suspend fun migrate(currentData: Session): Session {
                        val oldDs: DataStore<Preferences> =
                            PreferenceDataStoreFactory.create(
                                scope = CoroutineScope(ioDispatcher + SupervisorJob()),
                                produceFile = { context.preferencesDataStoreFile(DATASTORE_SESSION_PREFS_NAME) },
                            )
                        val prefs = oldDs.data.first()
                        val token = prefs[stringPreferencesKey("jwt_token")] ?: ""
                        val userId = prefs[stringPreferencesKey("user_id")] ?: ""
                        val verified = prefs[booleanPreferencesKey("verified")] ?: false
                        return if (token.isBlank() || userId.isBlank()) {
                            currentData
                        } else {
                            Session(token = token, userId = userId, verified = verified)
                        }
                    }

                    override suspend fun cleanUp() {}
                },
            ),
        scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        produceFile = { context.dataStoreFile(DATASTORE_SESSION_PREFS_NAME) },
    )
}
