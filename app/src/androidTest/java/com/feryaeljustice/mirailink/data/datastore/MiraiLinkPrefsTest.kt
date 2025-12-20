package com.feryaeljustice.mirailink.data.datastore

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.feryaeljustice.mirailink.di.koin.dataStoreTestModule
import com.google.common.truth.Truth.assertThat
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
class MiraiLinkPrefsTest : KoinTest {
    private lateinit var miraiLinkPrefs: MiraiLinkPrefs

    @get:Rule
    val koinTestRule =
        object : TestWatcher() {
            override fun starting(description: Description) {
                // Detener cualquier instancia previa de Koin
                stopKoin()

                // Iniciar Koin con el módulo de test
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
                // Limpiar Koin después del test
                stopKoin()

                // Limpiar archivos de cache de test
                val context =
                    InstrumentationRegistry
                        .getInstrumentation()
                        .targetContext
                        .applicationContext

                context.cacheDir
                    .listFiles { file -> file.name.startsWith("test_prefs_") }
                    ?.forEach { it.delete() }
            }
        }

    @Before
    fun setup() {
        miraiLinkPrefs = get()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun markOnboardingCompleted_updatesDataStore() =
        runBlocking {
            // When
            miraiLinkPrefs.markOnboardingCompleted()

            // Then
            val prefs = miraiLinkPrefs.isOnboardingCompleted()
            assertThat(prefs).isTrue()
        }

    @Test
    fun isOnboardingCompleted_readsInitialValue() =
        runBlocking {
            // When
            val prefs = miraiLinkPrefs.isOnboardingCompleted()

            // Then
            assertThat(prefs).isFalse()
        }
}
