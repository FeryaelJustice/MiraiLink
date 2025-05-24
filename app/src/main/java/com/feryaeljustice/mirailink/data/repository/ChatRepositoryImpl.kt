package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import org.json.JSONObject
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val socketService: SocketService,
    private val remote: ChatRemoteDataSource
) : ChatRepository {

    override fun connectSocket() = socketService.connect()

    override fun disconnectSocket() = socketService.disconnect()

    override suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> {
        return remote.createPrivateChat(otherUserId)
    }

    override suspend fun createGroupChat(name: String, userIds: List<UserDto>): MiraiLinkResult<String> {
        return remote.createGroupChat(name, userIds)
    }

    override fun sendMessage(message: String) {
        val json = JSONObject().apply {
            put("message", message)
        }
        socketService.emit("send_message", json)
    }

    override fun listenForMessages(callback: (String) -> Unit) {
        socketService.on("receive_message") { args ->
            val msg = args.firstOrNull()?.toString() ?: return@on
            callback(msg)
        }
    }
}
