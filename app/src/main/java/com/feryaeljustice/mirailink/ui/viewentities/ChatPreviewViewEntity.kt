package com.feryaeljustice.mirailink.ui.viewentities

data class ChatPreviewViewEntity(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val lastMessage: String,
    val isBoosted: Boolean = false
)