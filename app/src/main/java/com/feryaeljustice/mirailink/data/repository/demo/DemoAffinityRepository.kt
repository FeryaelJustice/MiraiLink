package com.feryaeljustice.mirailink.data.repository.demo

import androidx.datastore.core.DataStore
import androidx.room.withTransaction
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.entity.*
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.model.affinity.*
import com.feryaeljustice.mirailink.domain.repository.AffinityRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.*
import java.time.Instant
import java.util.UUID
import java.io.IOException

/** Demo uses real seeded Room peers and separate encrypted preferences, never the live API. */
class DemoAffinityRepository(private val db: MiraiLinkDemoDatabase, private val prefs: DataStore<AppPrefs>, private val session: GlobalMiraiLinkSession) : AffinityRepository {
    private val mutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }
    private suspend fun state() = prefs.data.first().demoAffinity
    private suspend fun update(transform: (AffinityDemoState) -> AffinityDemoState) { prefs.updateData { it.copy(demoAffinity = transform(it.demoAffinity)) } }
    private fun expiry() = Instant.now().plusSeconds(7 * 86400).toString()
    private fun peer(u: DemoFeedUserEntity): AffinityPerson {
        val avatar = runCatching { json.parseToJsonElement(u.photosJson).jsonArray.firstOrNull()?.jsonObject?.get("url")?.jsonPrimitive?.content }.getOrNull()
        return AffinityPerson(u.id, u.username, u.nickname, avatar)
    }
    private suspend fun initialize() {
        if (state().initialized) return
        val candidate = db.userDao().getFeedUsers().firstOrNull { it.nickname == "Kenji" }
            ?: db.userDao().getFeedUsers().firstOrNull()
        update { current ->
            val incomingList = candidate?.let { u ->
                listOf(
                    AffinityRequest(
                        id = "demo-incoming",
                        incoming = true,
                        state = "pending",
                        text = "🎮 Vi que compartimos gustos en RPGs, ¿hablamos?",
                        expiresAt = expiry(),
                        person = peer(u),
                    ),
                )
            } ?: emptyList()
            current.copy(
                initialized = true,
                requests = (incomingList + current.requests).distinctBy { it.id },
            )
        }
    }
    override suspend fun feed(): MiraiLinkResult<AffinityFeed> {
        initialize(); val s = state(); val users = db.userDao().getFeedUsers()
        val matchedIds = db.matchDao().getAllMatches().map { it.userId }.toSet()
        val requestPeerIds = s.requests.map { it.person.id }.toSet()
        val affinityLikedPeerIds = db.userDao().getFeedUsers().filter { it.nickname == "Leo" }.map { it.id }.toSet()
        val normalLikesPeerIds = db.userDao().getFeedUsers().filter { it.nickname in listOf("Ren", "Sakura") }.map { it.id }.toSet()

        val rows = if (s.participating) users.filter {
            it.id !in s.dismissed &&
            it.id !in s.blocked &&
            it.id !in s.liked &&
            it.id !in requestPeerIds &&
            it.id !in affinityLikedPeerIds &&
            it.id !in normalLikesPeerIds &&
            it.id !in matchedIds
        }.take(3).map { u ->
            val names = runCatching { (json.parseToJsonElement(u.animesJson).jsonArray + json.parseToJsonElement(u.gamesJson).jsonArray).mapNotNull { it.jsonObject["name"]?.jsonPrimitive?.content }.take(3) }.getOrDefault(emptyList())
            AffinityRecommendation(u.id, expiry(), if (u.id in s.liked) "liked" else "available", names, if (session.isPlus.value) peer(u) else null)
        } else emptyList()
        return MiraiLinkResult.Success(AffinityFeed(true, s.participating, true, rows))
    }
    override suspend fun likes(offset: Int): MiraiLinkResult<AffinityLikes> {
        initialize(); val s = state()
        val candidate = db.userDao().getFeedUsers().firstOrNull { it.nickname == "Leo" && it.id !in s.liked && it.id !in s.blocked }
            ?: db.userDao().getFeedUsers().lastOrNull { it.id !in s.liked && it.id !in s.blocked }
        return MiraiLinkResult.Success(AffinityLikes(if (candidate != null && offset == 0) listOf(AffinityLike(candidate.id, if (session.isPlus.value) peer(candidate) else null)) else emptyList()))
    }
    override suspend fun requests(offset: Int): MiraiLinkResult<AffinityRequests> {
        initialize(); val s = state(); return MiraiLinkResult.Success(AffinityRequests(s.requests.filter {
            it.person.id !in s.blocked && it.state == "pending" && Instant.parse(it.expiresAt).isAfter(Instant.now())
        }.drop(offset).take(20)))
    }
    override suspend fun activity() = MiraiLinkResult.Success(Unit)
    override suspend fun participate(enabled: Boolean): MiraiLinkResult<Unit> { update { it.copy(participating = enabled) }; return MiraiLinkResult.Success(Unit) }
    override suspend fun dismiss(id: String): MiraiLinkResult<Unit> { update { it.copy(dismissed = it.dismissed + id) }; return MiraiLinkResult.Success(Unit) }
    override suspend fun like(id: String): MiraiLinkResult<AffinityAction> {
        if (!session.isPlus.value) return MiraiLinkResult.Error(DataError.Network.FORBIDDEN)
        val user = db.userDao().getFeedUserById(id) ?: return MiraiLinkResult.Error(DataError.Local.NOT_FOUND)
        update {
            it.copy(
                liked = it.liked + id,
                requests = it.requests + AffinityRequest(
                    id = UUID.randomUUID().toString(),
                    incoming = false,
                    state = "pending",
                    text = "",
                    expiresAt = expiry(),
                    person = peer(user),
                ),
            )
        }
        db.userDao().markLiked(id)
        return MiraiLinkResult.Success(AffinityAction())
    }
    override suspend fun returnLike(id: String): MiraiLinkResult<AffinityAction> {
        if (!session.isPlus.value) return MiraiLinkResult.Error(DataError.Network.FORBIDDEN)
        db.matchDao().insertMatch(DemoMatchEntity(id, System.currentTimeMillis()));update { it.copy(liked = it.liked + id, dismissed = it.dismissed + id) }
        return MiraiLinkResult.Success(AffinityAction(match = true))
    }
    override suspend fun request(id: String, clientId: String, text: String): MiraiLinkResult<AffinityAction> = mutex.withLock {
        if (!session.isPlus.value) return@withLock MiraiLinkResult.Error(DataError.Network.FORBIDDEN)
        val s = state(); val previous = s.requests.find { it.id == clientId }
        if (previous != null) return@withLock MiraiLinkResult.Success(AffinityAction(previous.id, previous.state))
        if (s.requests.count { !it.incoming } >= 3 || s.requests.any { !it.incoming && it.person.id == id }) return@withLock MiraiLinkResult.Error(DataError.Network.RATE_LIMITED)
        val user = db.userDao().getFeedUserById(id) ?: return@withLock MiraiLinkResult.Error(DataError.Local.NOT_FOUND)
        update { it.copy(requests = it.requests + AffinityRequest(clientId, false, "pending", text, expiry(), peer(user))) }
        MiraiLinkResult.Success(AffinityAction(clientId, "pending"))
    }
    override suspend fun respond(id: String, accept: Boolean): MiraiLinkResult<AffinityAction> = mutex.withLock {
        val r = state().requests.find { it.id == id && it.incoming } ?: return@withLock MiraiLinkResult.Error(DataError.Local.NOT_FOUND)
        if (r.state != "pending") return@withLock MiraiLinkResult.Success(AffinityAction(id, r.state, r.chatId))
        var chatId: String? = null
        if (accept) db.withTransaction {
            val now = System.currentTimeMillis(); chatId = db.chatDao().getChatByUserId(r.person.id)?.id ?: "chat_${r.person.id}"
            db.chatDao().insertOrUpdateChat(DemoChatEntity(chatId!!, r.person.id, r.text, r.person.id, now))
            db.chatDao().insertMessage(DemoMessageEntity("affinity_$id", chatId!!, r.person.id, DemoDataSeeder.DEMO_USER_ID, r.text, now))
        }
        val next = if (accept) "accepted" else "rejected"
        update { it.copy(requests = it.requests.map { item -> if (item.id == id) item.copy(state = next, chatId = chatId) else item }) }
        MiraiLinkResult.Success(AffinityAction(id, next, chatId))
    }
    override suspend fun block(peerId: String): MiraiLinkResult<Unit> { update { it.copy(blocked = it.blocked + peerId) }; return MiraiLinkResult.Success(Unit) }
    override suspend fun contact(peerId: String): MiraiLinkResult<AffinityContact> = MiraiLinkResult.Success(AffinityContact(if (state().requests.any { it.person.id == peerId && it.state == "accepted" }) "affinity" else "legacy", db.matchDao().getMatchByUserId(peerId) != null))
    override suspend fun resetDemo(): MiraiLinkResult<Unit> {
        val outgoing = state().requests.filter { !it.incoming }
        update { AffinityDemoState(participating = true, initialized = false, requests = outgoing) }
        initialize()
        return MiraiLinkResult.Success(Unit)
    }
}
