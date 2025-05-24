package com.feryaeljustice.mirailink.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.feryaeljustice.mirailink.data.model.response.MinimalUserInfo
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.ChatSummary
import com.feryaeljustice.mirailink.domain.util.parseDate

fun ChatSummaryResponse.toDomain() = ChatSummary(
    id = id,
    type = ChatType.fromString(type),
    createdBy = createdBy,
    createdAt = parseDate(createdAt),
    joinedAt = parseDate(joinedAt),
    role = ChatRole.fromString(role),
    lastMessageId = lastMessageId,
    lastMessageText = lastMessageText,
    lastMessageSenderId = lastMessageSenderId,
    lastMessageSentAt = parseDate(lastMessageSentAt),
    unreadCount = unreadCount.toIntOrNull() ?: 0,
    destinatary = destinatary?.let {
        MinimalUserInfo(
            id = it.id ?: "",
            name = it.name ?: "Desconocido",
            avatarUrl = it.avatarUrl.orEmpty()
        )
    }
)