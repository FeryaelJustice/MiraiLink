// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import com.feryaeljustice.mirailink.di.koin.Qualifiers
import com.feryaeljustice.mirailink.di.koin.dataStoreTestModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
class SessionManagerTest : KoinTest {
    private lateinit var dataStore: DataStore<Session>
    private lateinit var sessionManager: SessionManager

    @get:Rule
    val koinTestRule =
        object : TestWatcher() {
            override fun starting(description: Description) {
                stopKoin()

                startKoin {
                    androidContext(
                        InstrumentationRegistry
                            .getInstrumentation()
                            .targetContext
                            .applicationContext,
                    )
                    modules(dataStoreTestModule)
                }
            }

            override fun finished(description: Description) {
                stopKoin()

                // Limpiar archivos de test
                val context =
                    InstrumentationRegistry
                        .getInstrumentation()
                        .targetContext
                        .applicationContext

                context.cacheDir
                    .listFiles { file ->
                        file.name.startsWith("test_app_prefs_") ||
                            file.name.startsWith("test_session_")
                    }?.forEach { it.delete() }
            }
        }

    @Before
    fun setup() {
        dataStore = get(qualifier = Qualifiers.SessionDataStore)
        sessionManager = get()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun saveSession_updatesDataStore() =
        runBlocking {
            // When
            sessionManager.saveSession("test-token", "test-user", true)

            // Then
            val session = dataStore.data.first()
            assertThat(session.token).isEqualTo("test-token")
            assertThat(session.userId).isEqualTo("test-user")
            assertThat(session.verified).isTrue()
        }

    @Test
    fun saveIsVerified_updatesDataStore() =
        runBlocking {
            // Given
            sessionManager.saveSession("test-token", "test-user", false)

            // When
            sessionManager.saveIsVerified(true)

            // Then
            val session = dataStore.data.first()
            assertThat(session.verified).isTrue()
        }

    @Test
    fun clearSession_clearsDataStore() =
        runBlocking {
            // Given
            sessionManager.saveSession("test-token", "test-user", true)

            // When
            sessionManager.clearSession()

            // Then
            val session = dataStore.data.first()
            assertThat(session.token).isEmpty()
            assertThat(session.userId).isEmpty()
            assertThat(session.verified).isFalse()
        }

    @Test
    fun getCurrentToken_returnsCorrectToken() =
        runBlocking {
            // Given
            sessionManager.saveSession("test-token", "test-user", true)

            // When
            val token = sessionManager.getCurrentToken()

            // Then
            assertThat(token).isEqualTo("test-token")
        }
}
