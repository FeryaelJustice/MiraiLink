package com.feryaeljustice.mirailink.data.model.response.chat

import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatSummaryResponse(
    @SerialName("chat_id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("created_by") val createdBy: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("joined_at") val joinedAt: String,
    @SerialName("role") val role: String,
    @SerialName("last_message_id") val lastMessageId: String? = null,
    @SerialName("last_message_text") val lastMessageText: String? = null,
    @SerialName("last_message_sender_id") val lastMessageSenderId: String? = null,
    @SerialName("last_message_sent_at") val lastMessageSentAt: String? = null,
    @SerialName("unread_count") val unreadCount: String,
    @SerialName("destinatary") val destinatary: MinimalUserInfoResponse? = null
)
