package com.feryaeljustice.mirailink.domain.model.capsule

import kotlinx.serialization.Serializable

@Serializable
data class PhotoPresentation(val capsuleId: String? = null, val level: Int = 0,
    val status: String = "active", val revision: Int = 0, val veiled: Boolean = true)

@Serializable
data class CapsuleQuestion(val questionId: String = "", val instanceId: String = "",
    val category: String = "anime", @kotlinx.serialization.SerialName("textEs") val es: String = "", @kotlinx.serialization.SerialName("textEn") val en: String = "",
    val answeredBy: List<String> = emptyList(), val completed: Boolean = false)

@Serializable
data class CrystalCapsule(val id: String, val userIds: List<String>, val status: String = "active",
    val progress: Int = 0, val level: Int = 0, val revision: Int = 0, val rulesVersion: Int = 1,
    val resumeAccepted: List<String> = emptyList(), val revealRequestedBy: String? = null,
    val question: CapsuleQuestion? = null, val seenQuestionIds: List<String> = emptyList(),
    val completedQuestionIds: List<String> = emptyList()) {
    fun photoPresentation() = PhotoPresentation(id, level, status, revision, status != "revealed")
}

@Serializable
data class CapsuleAction(val actionId: String, val expectedRevision: Int, val type: String,
    val category: String? = null, val missionId: String? = null, val text: String? = null)
