package com.feryaeljustice.mirailink.domain.usecase.chat

data class ChatUseCases(
    val connect: ConnectSocketUseCase,
    val disconnect: DisconnectSocketUseCase,
    val getChatsFromUser: GetChatsFromUser,
    val createPrivateChatUseCase: CreatePrivateChatUseCase,
    val createGroupChatUseCase: CreateGroupChatUseCase,
    val sendMessage: SendMessageUseCase,
    val listenMessages: ListenForMessagesUseCase,
)
