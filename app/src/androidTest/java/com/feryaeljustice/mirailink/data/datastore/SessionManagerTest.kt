// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import com.feryaeljustice.mirailink.di.SessionDataStore
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SessionManagerTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @SessionDataStore
    lateinit var dataStore: DataStore<Session>

    @Inject
    lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        hiltRule.inject()
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
