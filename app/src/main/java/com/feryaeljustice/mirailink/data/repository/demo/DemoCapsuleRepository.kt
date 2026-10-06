package com.feryaeljustice.mirailink.data.repository.demo

import android.content.Context
import androidx.room.withTransaction
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoCapsuleEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.model.capsule.*
import com.feryaeljustice.mirailink.domain.repository.*
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.util.UUID

/** Local simulated peer. All state changes and message credits are committed together. */
class DemoCapsuleRepository(
    private val db: MiraiLinkDemoDatabase,
    private val chat: ChatRepository,
    context: Context,
) : CapsuleRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val own = DemoDataSeeder.DEMO_USER_ID
    private val questions = json.parseToJsonElement(context.resources.openRawResource(R.raw.crystal_capsule_catalog)
        .bufferedReader().use { it.readText() }).jsonObject.getValue("questions").jsonArray
    private fun encodeState(state: CrystalCapsule, lastSpeaker: String?): String = JsonObject(
        json.encodeToJsonElement(state).jsonObject + ("_lastSpeaker" to (lastSpeaker?.let(::JsonPrimitive) ?: JsonNull))
    ).toString()

    override suspend fun available() = true
    override suspend fun cached(peerId: String) = db.capsuleDao().get(peerId)?.let { json.decodeFromString<CrystalCapsule>(it.snapshot) }
    override suspend fun history(peerId: String): MiraiLinkResult<CapsuleConversation> = db.withTransaction {
        var row = db.capsuleDao().get(peerId)
        if (row != null) {
            var state = json.decodeFromString<CrystalCapsule>(row.snapshot)
            val processed = json.decodeFromString<List<String>>(row.processedIds).toMutableSet()
            var pending = row.pendingSender
            var lastSpeaker = json.parseToJsonElement(row.snapshot).jsonObject["_lastSpeaker"]?.jsonPrimitive?.contentOrNull
            var ownText = row.lastOwnText
            var peerText = row.lastPeerText
            db.chatDao().getMessagesBetween(own, peerId).forEach { message ->
                if (processed.add(message.id) && state.status == "active" && !message.content.startsWith("[GESTURE_ROULETTE:", true)) {
                    val text = message.content.trim().lowercase().replace(Regex("\\s+"), " ")
                    if (text.isNotEmpty() && text != if(message.senderId == own) ownText else peerText) {
                        if (message.senderId == own) ownText = text else peerText = text
                        if (lastSpeaker != message.senderId && pending != null && pending != message.senderId) {
                            val progress = (state.progress + 1).coerceAtMost(8)
                            state = state.copy(progress = progress, level = progress / 2,
                                status = if(progress == 8) "revealed" else state.status, revision = state.revision + 1)
                            pending = null
                        } else if (lastSpeaker != message.senderId) pending = message.senderId
                        lastSpeaker = message.senderId
                    }
                }
            }
            row = row.copy(snapshot = encodeState(state, lastSpeaker), processedIds = json.encodeToString(processed.toList()),
                pendingSender = pending, lastOwnText = ownText, lastPeerText = peerText)
            db.capsuleDao().save(row)
        }
        when (val messages = chat.getMessagesWith(peerId)) {
            is MiraiLinkResult.Error -> messages
            is MiraiLinkResult.Success -> MiraiLinkResult.Success(CapsuleConversation(messages.data, row?.let { json.decodeFromString(it.snapshot) }))
        }
    }

    override suspend fun act(id: String, action: CapsuleAction): MiraiLinkResult<CrystalCapsule> = db.withTransaction {
        val row = db.capsuleDao().all().firstOrNull { json.decodeFromString<CrystalCapsule>(it.snapshot).id == id }
            ?: return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        var state = json.decodeFromString<CrystalCapsule>(row.snapshot)
        val ids = json.decodeFromString<List<String>>(row.actionIds)
        if(action.actionId in ids) return@withTransaction MiraiLinkResult.Success(state)
        if(action.expectedRevision != state.revision || state.status == "revealed") return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        var processed = json.decodeFromString<List<String>>(row.processedIds)
        when(action.type) {
            "pause" -> state = state.copy(status = "paused", resumeAccepted = emptyList(), revealRequestedBy = null)
            "leave" -> state = state.copy(status = "left", resumeAccepted = emptyList(), revealRequestedBy = null)
            "resume" -> state = state.copy(status = "active", resumeAccepted = listOf(own, row.peerId))
            "request_reveal", "accept_reveal" -> state = state.copy(status = "revealed", level = 4, revealRequestedBy = null)
            "cancel_reveal", "decline_reveal" -> state = state.copy(revealRequestedBy = null)
            "question" -> {
                if(state.status != "active") return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                val pool = questions.filter { it.jsonObject.getValue("category").jsonPrimitive.content == action.category }
                val q = pool.firstOrNull { it.jsonObject.getValue("id").jsonPrimitive.content !in state.seenQuestionIds } ?: pool.firstOrNull()
                    ?: return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                val obj = q.jsonObject
                val questionId = obj.getValue("id").jsonPrimitive.content
                state = state.copy(question = CapsuleQuestion(questionId, UUID.randomUUID().toString(), action.category!!,
                    obj.getValue("textEs").jsonPrimitive.content, obj.getValue("textEn").jsonPrimitive.content), seenQuestionIds = (state.seenQuestionIds + questionId).distinct())
            }
            "answer" -> {
                val q = state.question ?: return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                if(state.status != "active" || q.completed || q.instanceId != action.missionId || action.text.isNullOrBlank())
                    return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                val now = System.currentTimeMillis()
                val answer = DemoMessageEntity(action.actionId, "chat_${row.peerId}", own, row.peerId, action.text, now)
                val reply = DemoMessageEntity(UUID.randomUUID().toString(), "chat_${row.peerId}", row.peerId, own,
                    "[Demo] ${q.es} - Me gustaría compartir esta aventura contigo.", now + 1)
                db.chatDao().insertMessage(answer)
                db.chatDao().insertMessage(reply)
                processed = processed + listOf(answer.id, reply.id)
                val progress = (state.progress + if(q.questionId in state.completedQuestionIds) 0 else 2).coerceAtMost(8)
                state = state.copy(progress = progress, level = progress / 2, status = if(progress == 8) "revealed" else "active",
                    question = q.copy(completed = true, answeredBy = listOf(own, row.peerId)), completedQuestionIds = (state.completedQuestionIds + q.questionId).distinct())
            }
            else -> return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        }
        state = state.copy(revision = state.revision + 1)
        val resetTurn = action.type in listOf("pause", "leave", "resume") || state.status == "revealed"
        val lastSpeaker = if (resetTurn) null else json.parseToJsonElement(row.snapshot).jsonObject["_lastSpeaker"]?.jsonPrimitive?.contentOrNull
        db.capsuleDao().save(row.copy(snapshot = encodeState(state, lastSpeaker), processedIds = json.encodeToString(processed),
            pendingSender = if (resetTurn) null else row.pendingSender, actionIds = json.encodeToString(ids + action.actionId)))
        MiraiLinkResult.Success(state)
    }

    override suspend fun send(peerId: String, text: String, clientId: String): MiraiLinkResult<Unit> {
        if(db.chatDao().getMessagesBetween(own, peerId).any { it.id == clientId }) return MiraiLinkResult.Success(Unit)
        // Legacy demo sender provides its existing simulated reply and updates chat summaries.
        return db.withTransaction { (chat as DemoChatRepositoryImpl).sendConfirmed(peerId, text, clientId) }
    }
}
