package com.feryaeljustice.mirailink.domain.model.chat

import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.util.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ChatSummary(
    val id: String,
    val type: ChatType,
    val createdBy: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val joinedAt: Date,
    val role: ChatRole,
    val lastMessageId: String? = null,
    val lastMessageText: String? = null,
    val lastMessageSenderId: String? = null,
    @Serializable(with = DateSerializer::class)
    val lastMessageSentAt: Date? = null,
    val unreadCount: Int,
    val destinatary: MinimalUserInfo,
)
