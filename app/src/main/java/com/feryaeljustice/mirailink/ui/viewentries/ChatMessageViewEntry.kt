package com.feryaeljustice.mirailink.ui.viewentries

data class ChatMessageViewEntry(
    val id: String,
    val sender: MinimalUserInfoViewEntry,
    val receiver: MinimalUserInfoViewEntry,
    val content: String,
    val timestamp: Long
)