package com.feryaeljustice.mirailink.domain.model.capsule

import kotlinx.serialization.Serializable

@Serializable
data class PhotoPresentation(
    val capsuleId: String? = null,
    val level: Int = 0,
    val status: String = "active",
    val revision: Int = 0,
    val veiled: Boolean = true,
)

@Serializable
data class CapsuleQuestion(
    val questionId: String = "",
    val instanceId: String = "",
    val category: String = "anime",
    @kotlinx.serialization.SerialName("textEs") val es: String = "",
    @kotlinx.serialization.SerialName("textEn") val en: String = "",
    val text: String = "",
    val localizedText: String? = null,
    val questionLanguage: String? = null,
    val authorId: String = "",
    val authorAnswer: String? = null,
    val authorLanguage: String? = null,
    val peerAnswer: String? = null,
    val peerLanguage: String? = null,
    val isCustom: Boolean = false,
    val answeredBy: List<String> = emptyList(),
    val completed: Boolean = false,
) {
    fun displayText(isSpanish: Boolean): String {
        return when {
            localizedText?.isNotBlank() == true -> localizedText
            isCustom && text.isNotBlank() -> text
            isSpanish && es.isNotBlank() -> es
            !isSpanish && en.isNotBlank() -> en
            text.isNotBlank() -> text
            es.isNotBlank() -> es
            else -> en
        }
    }
}

@Serializable
data class CompletedCapsuleQuestion(
    val questionId: String = "",
    val instanceId: String = "",
    val category: String = "anime",
    val text: String = "",
    val localizedText: String? = null,
    val questionLanguage: String? = null,
    val authorId: String = "",
    val authorAnswer: String = "",
    val authorLanguage: String? = null,
    val peerAnswer: String = "",
    val peerLanguage: String? = null,
    val completedAt: String = "",
) {
    fun displayText(): String {
        return localizedText?.takeIf { it.isNotBlank() } ?: text
    }
}

@Serializable
data class CrystalCapsule(
    val id: String,
    val userIds: List<String>,
    val status: String = "active",
    val progress: Int = 0,
    val level: Int = 0,
    val revision: Int = 0,
    val rulesVersion: Int = 2,
    val resumeAccepted: List<String> = emptyList(),
    val revealRequestedBy: String? = null,
    val question: CapsuleQuestion? = null,
    val seenQuestionIds: List<String> = emptyList(),
    val completedQuestionIds: List<String> = emptyList(),
    val completedQuestions: List<CompletedCapsuleQuestion> = emptyList(),
) {
    fun photoPresentation() = PhotoPresentation(id, level, status, revision, status != "revealed")
}

@Serializable
data class CapsuleAction(
    val actionId: String,
    val expectedRevision: Int,
    val type: String,
    val category: String? = null,
    val questionId: String? = null,
    val customQuestion: String? = null,
    val missionId: String? = null,
    val text: String? = null,
    val answer: String? = null,
    val isCustom: Boolean = false,
    val language: String? = null,
)
