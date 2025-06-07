package com.feryaeljustice.mirailink.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val sender: MinimalUserInfo,
    val receiver: MinimalUserInfo,
    val content: String,
    val timestamp: Long
)