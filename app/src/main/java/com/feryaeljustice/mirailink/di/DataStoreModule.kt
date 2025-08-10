package com.feryaeljustice.mirailink.di

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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val DATASTORE_PREFS_NAME = "mirailink_prefs"
    private const val DATASTORE_SESSION_PREFS_NAME = "session_prefs"

    @Provides
    @Singleton
    @PrefsDataStore
    fun providePrefsDataStore(
        @ApplicationContext context: Context,
        json: Json,
        keyProvider: SecretKeyProvider
    ): DataStore<AppPrefs> {
        return DataStoreFactory.create(
            serializer = EncryptedJsonSerializer(
                json = json,
                kSerializer = AppPrefs.serializer(),
                default = AppPrefs(),
                keyProvider = keyProvider
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile(DATASTORE_PREFS_NAME) }
        )
    }


    @Provides
    @Singleton
    @SessionDataStore
    fun provideSessionPrefsDataStore(
        @ApplicationContext context: Context,
        json: Json,
        keyProvider: SecretKeyProvider
    ): DataStore<Session> {
        return DataStoreFactory.create(
            serializer = EncryptedJsonSerializer(
                json = json,
                kSerializer = Session.serializer(),
                default = Session(),
                keyProvider = keyProvider
            ),
            migrations = listOf(
                // 1) Migración desde SharedPreferences "session_prefs"
                object : DataMigration<Session> {
                    override suspend fun shouldMigrate(currentData: Session) =
                        currentData == Session()

                    override suspend fun migrate(currentData: Session): Session {
                        val sp = context.getSharedPreferences(
                            DATASTORE_SESSION_PREFS_NAME,
                            Context.MODE_PRIVATE
                        )
                        val token = sp.getString("jwt_token", "") ?: ""
                        val userId = sp.getString("user_id", "") ?: ""
                        val verified = sp.getBoolean("verified", false)
                        return if (token.isBlank() || userId.isBlank()) currentData
                        else Session(token = token, userId = userId, verified = verified)
                    }

                    override suspend fun cleanUp() {
                    }
                },
                // 2) Migración desde tu Preferences DataStore actual (session_prefs.preferences_pb)
                object : DataMigration<Session> {
                    override suspend fun shouldMigrate(currentData: Session) =
                        currentData == Session()

                    override suspend fun migrate(currentData: Session): Session {
                        // Crea un DS de solo lectura apuntando al archivo viejo
                        val oldDs: DataStore<Preferences> = PreferenceDataStoreFactory.create(
                            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                            produceFile = {
                                context.preferencesDataStoreFile(
                                    DATASTORE_SESSION_PREFS_NAME
                                )
                            }
                        )
                        val prefs = oldDs.data.first()
                        val token = prefs[stringPreferencesKey("jwt_token")] ?: ""
                        val userId = prefs[stringPreferencesKey("user_id")] ?: ""
                        val verified = prefs[booleanPreferencesKey("verified")] ?: false
                        return if (token.isBlank() || userId.isBlank()) currentData
                        else Session(token = token, userId = userId, verified = verified)
                    }

                    override suspend fun cleanUp() {
                    }
                }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile(DATASTORE_SESSION_PREFS_NAME) }
        )
    }
}