package com.feryaeljustice.mirailink.ui.viewentries.chat

import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageViewEntry(
    val id: String,
    val sender: MinimalUserInfoViewEntry,
    val receiver: MinimalUserInfoViewEntry,
    val content: String,
    val timestamp: Long
)