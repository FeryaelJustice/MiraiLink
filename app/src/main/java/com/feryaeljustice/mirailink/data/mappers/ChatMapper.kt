package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.response.MinimalUserInfo
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.ChatMessage
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
    lastMessageSentAt = lastMessageSentAt?.let { parseDate(lastMessageSentAt) } ?: parseDate(
        createdAt
    ),
    unreadCount = unreadCount.toIntOrNull() ?: 0,
    destinatary =
        MinimalUserInfo(
            id = destinatary?.id ?: "",
            username = destinatary?.username ?: "Desconocido",
            avatarUrl = destinatary?.avatarUrl.orEmpty()
        )
)

fun ChatMessageResponse.toDomain(): ChatMessage = ChatMessage(
    id = id,
    sender = sender.toMinimalUserInfoDomain(),
    receiver = receiver.toMinimalUserInfoDomain(),
    content = content,
    timestamp = timestamp
)