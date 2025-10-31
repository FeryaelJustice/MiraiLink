package com.feryaeljustice.mirailink.data.model.request.chat

import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupChatRequest(
    val name: String,
    val userIds: List<String>,
)
