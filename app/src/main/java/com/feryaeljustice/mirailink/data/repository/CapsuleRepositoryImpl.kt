package com.feryaeljustice.mirailink.data.repository
import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import com.feryaeljustice.mirailink.data.remote.CapsuleApiService
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.model.capsule.*
import com.feryaeljustice.mirailink.domain.repository.*
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.first
import java.io.IOException
class CapsuleRepositoryImpl(private val api: CapsuleApiService, private val preferences: DataStore<AppPrefs>,
    private val session: SessionManager, private val environment: String) : CapsuleRepository {
    private suspend fun key(peer: String) = environment + ":" + session.userIdFlow.first().orEmpty() + ":" + peer
    override suspend fun available() = (safeApiCall(NetworkOperation.AUTHENTICATED) { api.config().enabled } as? MiraiLinkResult.Success)?.data == true
    override suspend fun cached(peerId: String) = try {
        preferences.data.first().capsuleSnapshots[key(peerId)]
    } catch (_: IOException) { null }
    private suspend fun persist(transform: (AppPrefs) -> AppPrefs) {
        // A disk failure must not turn an already confirmed server action into a failed retry.
        try { preferences.updateData { transform(it) } } catch (_: IOException) { }
    }
    override suspend fun history(peerId: String): MiraiLinkResult<CapsuleConversation> {
        val ownerKey = key(peerId)
        return when(val result = safeApiCall(NetworkOperation.AUTHENTICATED) { api.history(peerId) }) {
            is MiraiLinkResult.Error -> result
            is MiraiLinkResult.Success -> {
                // The captured owner key prevents a late response crossing accounts.
                persist { prefs -> prefs.copy(capsuleSnapshots = if(result.data.capsule == null)
                    prefs.capsuleSnapshots - ownerKey else prefs.capsuleSnapshots + (ownerKey to result.data.capsule)) }
                MiraiLinkResult.Success(CapsuleConversation(result.data.messages.map { it.toDomain() }, result.data.capsule))
            }
        }
    }
    override suspend fun act(id: String, action: CapsuleAction): MiraiLinkResult<CrystalCapsule> {
        val prefix = environment + ":" + session.userIdFlow.first().orEmpty() + ":"
        val result = safeApiCall(NetworkOperation.AUTHENTICATED) { api.action(id, action).capsule }
        if(result is MiraiLinkResult.Success) persist { prefs ->
            prefs.copy(capsuleSnapshots = prefs.capsuleSnapshots.mapValues { (key, value) ->
                if(key.startsWith(prefix) && value.id == id) result.data else value
            })
        }
        return result
    }
    override suspend fun send(peerId: String, text: String, clientId: String): MiraiLinkResult<Unit> =
        when (val result = safeApiCall(NetworkOperation.AUTHENTICATED) { api.send(ChatRequest(peerId, text, clientId)) }) {
            is MiraiLinkResult.Success -> MiraiLinkResult.Success(Unit)
            is MiraiLinkResult.Error -> result
        }
}
