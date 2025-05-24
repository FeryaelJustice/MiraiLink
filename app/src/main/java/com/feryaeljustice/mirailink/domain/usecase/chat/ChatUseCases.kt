package com.feryaeljustice.mirailink.domain.usecase.chat

import javax.inject.Inject

data class ChatUseCases @Inject constructor(
    val connect: ConnectSocketUseCase,
    val disconnect: DisconnectSocketUseCase,
    val getChatsFromUser: GetChatsFromUser,
    val createPrivateChatUseCase: CreatePrivateChatUseCase,
    val createGroupChatUseCase: CreateGroupChatUseCase,
    val sendMessage: SendMessageUseCase,
    val listenMessages: ListenForMessagesUseCase
)