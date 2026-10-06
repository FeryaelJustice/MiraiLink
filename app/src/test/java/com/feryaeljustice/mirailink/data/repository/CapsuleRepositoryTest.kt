package com.feryaeljustice.mirailink.data.repository

import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import com.feryaeljustice.mirailink.data.remote.CapsuleApiService
import com.feryaeljustice.mirailink.data.remote.CapsuleHistory
import com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.mappers.toMinimalUserInfo
import com.feryaeljustice.mirailink.data.mappers.ui.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

private class CapsuleMemoryStore<T>(initial: T) : DataStore<T> {
    val value = MutableStateFlow(initial)
    override val data: Flow<T> = value
    override suspend fun updateData(transform: suspend (T) -> T): T = transform(value.value).also { value.value = it }
}
class CapsuleRepositoryTest {
    @Test fun `legacy chat history remains usable before the capsule server rollout`() {
        val json = Json { ignoreUnknownKeys = true }
        assertEquals(CapsuleHistory(emptyList()), json.decodeFromString<CapsuleHistory>("[]"))
        assertEquals(CapsuleHistory(emptyList()), json.decodeFromString<CapsuleHistory>("""{"messages":[],"capsule":null}"""))
        assertEquals("", json.decodeFromString<com.feryaeljustice.mirailink.data.remote.ConfirmedMessage>("""{"message":"Message sent"}""").id)
    }
    @Test fun `cached snapshots are separated by account and environment and server null removes stale state`() = runTest {
        val prefs = CapsuleMemoryStore(AppPrefs())
        val sessions = CapsuleMemoryStore(Session())
        val session = SessionManager(sessions, backgroundScope)
        val api = mockk<CapsuleApiService>()
        val capsule = CrystalCapsule("capsule", listOf("alice", "peer"))
        coEvery { api.history("peer") } returns CapsuleHistory(emptyList(), capsule)
        sessions.updateData { it.copy(userId = "alice") }
        val real = CapsuleRepositoryImpl(api, prefs, session, "real")
        val otherEnvironment = CapsuleRepositoryImpl(api, prefs, session, "staging")
        real.history("peer")
        assertEquals(capsule, real.cached("peer")); assertNull(otherEnvironment.cached("peer"))
        sessions.updateData { it.copy(userId = "bob") }
        assertNull(real.cached("peer"))
        sessions.updateData { it.copy(userId = "alice") }
        assertEquals(capsule, real.cached("peer"))
        coEvery { api.history("peer") } returns CapsuleHistory(emptyList(), null)
        real.history("peer")
        assertNull(real.cached("peer"))
    }
    @Test fun `veil metadata survives all user projections and older JSON stays classic`() {
        val json = Json { ignoreUnknownKeys = true }
        val old = json.decodeFromString<UserDto>("""{"id":"peer","nickname":"Player"}""")
        assertNull(old.toDomain().photoPresentation)
        val capsule = json.decodeFromString<UserDto>("""{"id":"peer","nickname":"Player","photoPresentation":{"capsuleId":"c","level":1,"status":"paused","revision":8,"veiled":true}}""")
        val domain = capsule.toDomain()
        assertTrue(domain.photoPresentation!!.veiled)
        assertEquals(domain.photoPresentation, domain.toUserViewEntry().photoPresentation)
        assertEquals(domain.photoPresentation, domain.toMinimalUserInfo().toMinimalUserInfoViewEntry().photoPresentation)
        assertEquals(domain.photoPresentation, domain.toMatchUserViewEntry().photoPresentation)
    }
}
