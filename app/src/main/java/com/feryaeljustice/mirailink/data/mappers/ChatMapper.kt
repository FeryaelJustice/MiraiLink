package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.util.parseDate

fun ChatSummaryResponse.toDomain() = ChatSummary(
    id = id,
    type = ChatType.fromString(type),
    createdBy = createdBy,
    createdAt = parseDate(createdAt),
    joinedAt = parseDate(joinedAt),
    role = ChatRole.fromString(role),
    lastMessageId = lastMessageId,
    lastMessageText = lastMessageText.orEmpty(),
    lastMessageSenderId = lastMessageSenderId,
    lastMessageSentAt = lastMessageSentAt?.let { parseDate(lastMessageSentAt) } ?: parseDate(
        createdAt
    ),
    unreadCount = unreadCount.toIntOrNull() ?: 0,
    destinatary = destinatary?.toMinimalUserInfo()
)

fun ChatMessageResponse.toDomain(): ChatMessage = ChatMessage(
    id = id,
    sender = sender.toMinimalUserInfo(),
    receiver = receiver.toMinimalUserInfo(),
    content = content,
    timestamp = timestamp
)