package com.feryaeljustice.mirailink.domain.model.chat

import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val sender: MinimalUserInfo,
    val receiver: MinimalUserInfo,
    val content: String,
    val timestamp: Long
)