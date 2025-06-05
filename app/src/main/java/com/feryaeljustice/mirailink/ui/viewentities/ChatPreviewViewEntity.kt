package com.feryaeljustice.mirailink.ui.viewentities

data class ChatPreviewViewEntity(
    val userId: String,
    val username: String,
    val nickname: String,
    val avatarUrl: String,
    val lastMessage: String? = null,
    val isBoosted: Boolean = false,
    val readsPending: Int = 0,
)