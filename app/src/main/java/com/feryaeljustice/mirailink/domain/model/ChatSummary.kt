package com.feryaeljustice.mirailink.domain.model

import com.feryaeljustice.mirailink.data.model.response.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import java.util.Date

data class ChatSummary(
    val id: String,
    val type: ChatType,
    val createdBy: String,
    val createdAt: Date,
    val joinedAt: Date,
    val role: ChatRole,
    val lastMessageId: String,
    val lastMessageText: String,
    val lastMessageSenderId: String,
    val lastMessageSentAt: Date,
    val unreadCount: Int,
    val destinatary: MinimalUserInfo?,
)
