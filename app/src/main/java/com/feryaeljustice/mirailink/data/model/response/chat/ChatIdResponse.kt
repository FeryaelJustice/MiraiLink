package com.feryaeljustice.mirailink.data.model.response.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatIdResponse(@SerialName("chatId") val chatId: String)