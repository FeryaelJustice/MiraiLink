package com.feryaeljustice.mirailink.data.model.response.chat

import com.feryaeljustice.mirailink.data.model.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerialName("id")
    val id: String,
    @SerialName("sender")
    val sender: UserDto,
    @SerialName("receiver")
    val receiver: UserDto,
    @SerialName("content")
    val content: String,
    @SerialName("timestamp")
    val timestamp: Long
)