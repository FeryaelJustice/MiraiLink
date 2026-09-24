package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPromptAnswerDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("prompt_id")
    val promptId: String,
    @SerialName("code")
    val code: String? = null,
    @SerialName("question")
    val question: String? = null,
    @SerialName("answer")
    val answer: String,
)
