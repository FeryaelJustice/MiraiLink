package com.feryaeljustice.mirailink.data.model.request.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    @SerialName("toUserId")
    val toUserId: String,
    @SerialName("text")
    val text: String
)