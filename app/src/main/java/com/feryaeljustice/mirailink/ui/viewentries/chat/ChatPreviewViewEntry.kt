package com.feryaeljustice.mirailink.ui.viewentries.chat

data class ChatPreviewViewEntry(
    val userId: String? = null,
    val username: String = "",
    val nickname: String = "",
    val avatarUrl: String = "",
    val lastMessage: String = "",
    val isBoosted: Boolean = false,
    val readsPending: Int = 0,
)