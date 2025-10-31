// Feryael Justice
// 2024-07-29

package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.di.DataStoreModule
import com.feryaeljustice.mirailink.di.PrefsDataStore
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@UninstallModules(DataStoreModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MiraiLinkPrefsTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @PrefsDataStore
    lateinit var dataStore: DataStore<AppPrefs>

    private lateinit var miraiLinkPrefs: MiraiLinkPrefs

    @Before
    fun setUp() {
        hiltRule.inject()
        miraiLinkPrefs = MiraiLinkPrefs(dataStore)
    }

    @Test
    fun markOnboardingCompleted_updatesDataStore() = runBlocking {
        // When
        miraiLinkPrefs.markOnboardingCompleted()

        // Then
        val prefs = miraiLinkPrefs.isOnboardingCompleted()
        assertThat(prefs).isTrue()
    }

    @Test
    fun isOnboardingCompleted_readsInitialValue() = runBlocking {
        // When
        val prefs = miraiLinkPrefs.isOnboardingCompleted()

        // Then
        assertThat(prefs).isFalse()
    }
}
