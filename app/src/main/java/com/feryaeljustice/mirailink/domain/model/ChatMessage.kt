package com.feryaeljustice.mirailink.domain.model

data class ChatMessage(
    val id: String,
    val sender: MinimalUserInfo,
    val receiver: MinimalUserInfo,
    val content: String,
    val timestamp: Long
)