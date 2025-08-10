package com.feryaeljustice.mirailink.ui.mappers

import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import com.feryaeljustice.mirailink.ui.viewentries.ChatMessageViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.ChatPreviewViewEntry

fun ChatSummary.toChatPreviewViewEntry(): ChatPreviewViewEntry = ChatPreviewViewEntry(
    userId = destinatary?.id,
    username = destinatary?.username.orEmpty(),
    nickname = destinatary?.nickname.orEmpty(),
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