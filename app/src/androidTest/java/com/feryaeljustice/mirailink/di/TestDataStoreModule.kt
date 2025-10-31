// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.feryaeljustice.mirailink.data.datastore.serializer.AppPrefsSerializer
import com.feryaeljustice.mirailink.data.datastore.serializer.SessionSerializer
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestDataStoreModule {

    @Provides
    @Singleton
    @PrefsDataStore
    fun providePrefsDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataStore<AppPrefs> {
        return DataStoreFactory.create(
            serializer = AppPrefsSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            produceFile = { context.dataStoreFile("test_app_prefs.pb") }
        )
    }

    @Provides
    @Singleton
    @SessionDataStore
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataStore<Session> {
        return DataStoreFactory.create(
            serializer = SessionSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            produceFile = { context.dataStoreFile("test_session_prefs.pb") }
        )
    }
}
