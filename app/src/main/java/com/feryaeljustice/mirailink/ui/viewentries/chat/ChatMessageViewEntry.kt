package com.feryaeljustice.mirailink.ui.viewentries.chat

import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry

data class ChatMessageViewEntry(
    val id: String,
    val sender: MinimalUserInfoViewEntry,
    val receiver: MinimalUserInfoViewEntry,
    val content: String,
    val timestamp: Long
)