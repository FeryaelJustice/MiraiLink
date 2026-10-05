package com.feryaeljustice.mirailink.data.repository

import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

/** Compatibilidad JSON y actualizaciones atomicas de la preferencia. */
class HoloPreferencesRepositoryTest {
    @Test fun `JSON anterior activa efecto sin perder otros ajustes`() {
        val prefs = Json.decodeFromString<AppPrefs>("""{"searchRadiusKm":75.0,"onboardingCompleted":true}""")
        assertTrue(prefs.holoProfileEnabled)
        assertEquals(75f, prefs.searchRadiusKm)
        assertTrue(prefs.onboardingCompleted)
    }

    @Test fun `guardar conserva otros campos y emite nuevo valor`() = runTest {
        val store = MemoryStore(AppPrefs(searchRadiusKm = 75f))
        val repo = HoloPreferencesRepositoryImpl(MiraiLinkPrefs(store))
        assertTrue(repo.observeEnabled().first())
        assertTrue(repo.setEnabled(false) is MiraiLinkResult.Success)
        assertFalse(repo.observeEnabled().first())
        assertEquals(75f, store.data.value.searchRadiusKm)
    }

    @Test fun `fallo de escritura no cambia valor persistido`() = runTest {
        val store = MemoryStore(AppPrefs(), IllegalStateException())
        val repo = HoloPreferencesRepositoryImpl(MiraiLinkPrefs(store))
        assertTrue(repo.setEnabled(false) is MiraiLinkResult.Error)
        assertTrue(repo.observeEnabled().first())
    }

    @Test fun `cancelacion de escritura se propaga`() = runTest {
        val repo = HoloPreferencesRepositoryImpl(MiraiLinkPrefs(MemoryStore(AppPrefs(), CancellationException())))
        try {
            repo.setEnabled(false)
            fail("Cancellation must propagate")
        } catch (expected: CancellationException) {
            assertNotNull(expected)
        }
    }

    private class MemoryStore(initial: AppPrefs, val failure: Exception? = null) : DataStore<AppPrefs> {
        override val data = MutableStateFlow(initial)
        override suspend fun updateData(transform: suspend (AppPrefs) -> AppPrefs): AppPrefs {
            failure?.let { throw it }
            return transform(data.value).also { data.value = it }
        }
    }
}
