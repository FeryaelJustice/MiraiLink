// Author: Feryael Justice
// Date: 2024-07-29

package com.feryaeljustice.mirailink.data.datastore

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MiraiLinkPrefsTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var miraiLinkPrefs: MiraiLinkPrefs

    @Before
    fun setUp() {
        hiltRule.inject()
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
