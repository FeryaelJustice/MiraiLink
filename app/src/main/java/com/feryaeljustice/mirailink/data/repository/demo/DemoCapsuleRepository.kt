package com.feryaeljustice.mirailink.data.repository.demo

import android.content.Context
import androidx.room.withTransaction
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.model.capsule.*
import com.feryaeljustice.mirailink.domain.repository.*
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.util.UUID

/** Local simulated peer. All state changes are committed atomically in Room. */
class DemoCapsuleRepository(
    private val db: MiraiLinkDemoDatabase,
    private val chat: ChatRepository,
    context: Context,
) : CapsuleRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val own = DemoDataSeeder.DEMO_USER_ID
    private val questions = json.parseToJsonElement(
        context.resources.openRawResource(R.raw.crystal_capsule_catalog)
            .bufferedReader().use { it.readText() },
    ).jsonObject.getValue("questions").jsonArray

    override suspend fun available() = true

    override suspend fun cached(peerId: String) =
        db.capsuleDao().get(peerId)?.let { json.decodeFromString<CrystalCapsule>(it.snapshot) }

    override suspend fun history(peerId: String): MiraiLinkResult<CapsuleConversation> = db.withTransaction {
        val row = db.capsuleDao().get(peerId)
        when (val messages = chat.getMessagesWith(peerId)) {
            is MiraiLinkResult.Error -> messages
            is MiraiLinkResult.Success -> MiraiLinkResult.Success(
                CapsuleConversation(messages.data, row?.let { json.decodeFromString(it.snapshot) }),
            )
        }
    }

    override suspend fun act(id: String, action: CapsuleAction): MiraiLinkResult<CrystalCapsule> = db.withTransaction {
        val row = db.capsuleDao().all().firstOrNull { json.decodeFromString<CrystalCapsule>(it.snapshot).id == id }
            ?: return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        var state = json.decodeFromString<CrystalCapsule>(row.snapshot)
        val ids = json.decodeFromString<List<String>>(row.actionIds)
        if (action.actionId in ids) return@withTransaction MiraiLinkResult.Success(state)
        if (action.expectedRevision != state.revision || state.status == "revealed") {
            return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        }

        when (action.type) {
            "pause" -> {
                state = state.copy(status = "paused", resumeAccepted = emptyList(), revealRequestedBy = null)
            }
            "leave" -> {
                state = state.copy(status = "left", resumeAccepted = emptyList(), revealRequestedBy = null)
            }
            "resume" -> {
                state = state.copy(status = "active", resumeAccepted = listOf(own, row.peerId))
            }
            "request_reveal", "accept_reveal" -> {
                state = state.copy(status = "revealed", progress = 4, level = 4, revealRequestedBy = null)
            }
            "cancel_reveal", "decline_reveal" -> {
                state = state.copy(revealRequestedBy = null)
            }
            "question" -> {
                if (state.status != "active") return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                // Strict turn check: cannot propose if there is already an active question
                if (state.question != null && !state.question.completed) {
                    return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                }
                val authorAns = (action.answer ?: action.text)?.trim()
                if (authorAns.isNullOrEmpty()) return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)

                val lang = action.language ?: "es"
                val (questionId, es, en) = if (action.isCustom) {
                    val customText = (action.customQuestion ?: action.text)?.trim()
                    if (customText.isNullOrEmpty()) return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                    Triple("custom_${UUID.randomUUID()}", customText, customText)
                } else {
                    val pool = questions.filter { it.jsonObject.getValue("category").jsonPrimitive.content == (action.category ?: "anime") }
                    val qObj = (action.questionId?.let { id -> pool.firstOrNull { it.jsonObject.getValue("id").jsonPrimitive.content == id } }
                        ?: pool.firstOrNull { it.jsonObject.getValue("id").jsonPrimitive.content !in state.seenQuestionIds }
                        ?: pool.firstOrNull())
                        ?: return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                    val obj = qObj.jsonObject
                    Triple(obj.getValue("id").jsonPrimitive.content, obj.getValue("textEs").jsonPrimitive.content, obj.getValue("textEn").jsonPrimitive.content)
                }

                // In demo mode: simulated peer immediately responds to complete the bilateral turn
                val qText = if (lang == "es") es else en
                val peerReply = "[Demo] $qText - ¡Qué buena respuesta! Me ha encantado conocer tu punto de vista."
                val newProgress = (state.progress + 1).coerceAtMost(4)
                val completed = CompletedCapsuleQuestion(
                    questionId = questionId,
                    instanceId = UUID.randomUUID().toString(),
                    category = action.category ?: "anime",
                    text = qText,
                    localizedText = qText,
                    questionLanguage = lang,
                    authorId = own,
                    authorAnswer = authorAns,
                    authorLanguage = lang,
                    peerAnswer = peerReply,
                    peerLanguage = lang,
                    completedAt = System.currentTimeMillis().toString(),
                )

                val isRevealed = newProgress == 4
                state = state.copy(
                    progress = newProgress,
                    level = newProgress,
                    status = if (isRevealed) "revealed" else "active",
                    question = null,
                    seenQuestionIds = (state.seenQuestionIds + questionId).distinct(),
                    completedQuestionIds = (state.completedQuestionIds + questionId).distinct(),
                    completedQuestions = state.completedQuestions + completed,
                )
            }
            "answer" -> {
                val q = state.question ?: return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                if (state.status != "active" || q.completed) return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
                val answerText = (action.answer ?: action.text)?.trim()
                if (answerText.isNullOrEmpty()) return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)

                val lang = action.language ?: "es"
                val qText = q.displayText(lang == "es")
                val newProgress = (state.progress + 1).coerceAtMost(4)
                val completed = CompletedCapsuleQuestion(
                    questionId = q.questionId,
                    instanceId = q.instanceId,
                    category = q.category,
                    text = qText,
                    localizedText = qText,
                    questionLanguage = q.questionLanguage ?: lang,
                    authorId = q.authorId,
                    authorAnswer = q.authorAnswer ?: "",
                    authorLanguage = q.authorLanguage ?: lang,
                    peerAnswer = answerText,
                    peerLanguage = lang,
                    completedAt = System.currentTimeMillis().toString(),
                )
                val isRevealed = newProgress == 4
                state = state.copy(
                    progress = newProgress,
                    level = newProgress,
                    status = if (isRevealed) "revealed" else "active",
                    question = null,
                    completedQuestionIds = (state.completedQuestionIds + q.questionId).distinct(),
                    completedQuestions = state.completedQuestions + completed,
                )
            }
            else -> return@withTransaction MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        }

        state = state.copy(revision = state.revision + 1)
        db.capsuleDao().save(
            row.copy(
                snapshot = json.encodeToString(state),
                actionIds = json.encodeToString(ids + action.actionId),
            ),
        )
        MiraiLinkResult.Success(state)
    }

    override suspend fun send(peerId: String, text: String, clientId: String): MiraiLinkResult<Unit> {
        if (db.chatDao().getMessagesBetween(own, peerId).any { it.id == clientId }) return MiraiLinkResult.Success(Unit)
        return db.withTransaction { (chat as DemoChatRepositoryImpl).sendConfirmed(peerId, text, clientId) }
    }
}
