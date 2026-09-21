package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatPreviewViewEntry

fun ChatSummary.toChatPreviewViewEntry(): ChatPreviewViewEntry = ChatPreviewViewEntry(
    userId = destinatary?.id,
    chatId = id,
    isGroup = type == com.feryaeljustice.mirailink.domain.enums.ChatType.GROUP,
    username = destinatary?.username ?: name.orEmpty(),
    nickname = destinatary?.nickname ?: name.orEmpty(),
    avatarUrl = destinatary?.profilePhoto?.url.getFormattedUrl(),
    lastMessage = lastMessageText,
    isBoosted = false,
    readsPending = unreadCount
)

fun ChatMessage.toChatMessageViewEntry(): ChatMessageViewEntry = ChatMessageViewEntry(
    id = id,
    sender = sender.toMinimalUserInfoViewEntry(),
    receiver = receiver.toMinimalUserInfoViewEntry(),
    content = content,
    timestamp = timestamp,
)
