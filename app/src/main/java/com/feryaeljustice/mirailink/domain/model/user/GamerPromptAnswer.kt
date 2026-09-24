package com.feryaeljustice.mirailink.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class GamerPromptAnswer(
    val promptId: String,
    val question: String,
    val answer: String,
)
