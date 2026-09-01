package com.feryaeljustice.mirailink.data.repository.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.toDomainChatMessage
import com.feryaeljustice.mirailink.data.local.demo.toDomainChatSummary
import com.feryaeljustice.mirailink.data.local.demo.toMinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class DemoChatRepositoryImpl(
    private val database: MiraiLinkDemoDatabase,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : ChatRepository {

    override fun connectSocket() {
        // Modo offline: no requiere conexión a servidor Socket.IO
    }

    override fun disconnectSocket() {
        // Modo offline
    }

    override suspend fun getChatsFromUser(): MiraiLinkResult<List<ChatSummary>> {
        val chats = database.chatDao().getAllChats()
        val chatSummaries = chats.map { chat ->
            val otherUser = database.userDao().getFeedUserById(chat.otherUserId)?.toMinimalUserInfo()
                ?: MinimalUserInfo(
                    id = chat.otherUserId,
                    username = "usuario_demo",
                    nickname = "Usuario Demo",
                )
            chat.toDomainChatSummary(otherUser)
        }
        return MiraiLinkResult.Success(chatSummaries)
    }

    override suspend fun markChatAsRead(chatId: String): MiraiLinkResult<Unit> {
        database.chatDao().markChatAsRead(chatId)
        return MiraiLinkResult.Success(Unit)
    }

    override suspend fun createPrivateChat(otherUserId: String): MiraiLinkResult<String> {
        val chatId = "chat_$otherUserId"
        val existingChat = database.chatDao().getChatByUserId(otherUserId)
        if (existingChat == null) {
            val now = System.currentTimeMillis()
            val newChat = DemoChatEntity(
                id = chatId,
                otherUserId = otherUserId,
                lastMessageText = "Conversación iniciada",
                lastMessageSenderId = DemoDataSeeder.DEMO_USER_ID,
                lastMessageTimestamp = now,
                unreadCount = 0,
            )
            database.chatDao().insertOrUpdateChat(newChat)
        }
        return MiraiLinkResult.Success(chatId)
    }

    override suspend fun createGroupChat(name: String, userIds: List<String>): MiraiLinkResult<String> {
        return MiraiLinkResult.Success("group_chat_demo")
    }

    override suspend fun getMessagesWith(userId: String): MiraiLinkResult<List<ChatMessage>> {
        val myProfile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        val senderInfo = myProfile?.toMinimalUserInfo() ?: MinimalUserInfo(
            id = DemoDataSeeder.DEMO_USER_ID,
            username = "hikari_demo",
            nickname = "Hikari",
        )

        val feedUser = database.userDao().getFeedUserById(userId)
        val receiverInfo = feedUser?.toMinimalUserInfo() ?: MinimalUserInfo(
            id = userId,
            username = "amigo_demo",
            nickname = "Amigo Demo",
        )

        val entities = database.chatDao().getMessagesBetween(DemoDataSeeder.DEMO_USER_ID, userId)
        val domainMessages = entities.map { msg ->
            val msgSender = if (msg.senderId == DemoDataSeeder.DEMO_USER_ID) senderInfo else receiverInfo
            val msgReceiver = if (msg.senderId == DemoDataSeeder.DEMO_USER_ID) receiverInfo else senderInfo
            msg.toDomainChatMessage(msgSender, msgReceiver)
        }

        return MiraiLinkResult.Success(domainMessages)
    }

    override suspend fun sendMessageTo(userId: String, content: String): MiraiLinkResult<Unit> {
        val chatId = "chat_$userId"
        val now = System.currentTimeMillis()

        // 1. Guardar mensaje enviado por el usuario demo
        val myMessage = DemoMessageEntity(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = DemoDataSeeder.DEMO_USER_ID,
            receiverId = userId,
            content = content,
            timestamp = now,
            isRead = true,
        )
        database.chatDao().insertMessage(myMessage)

        val updatedChat = DemoChatEntity(
            id = chatId,
            otherUserId = userId,
            lastMessageText = content,
            lastMessageSenderId = DemoDataSeeder.DEMO_USER_ID,
            lastMessageTimestamp = now,
            unreadCount = 0,
        )
        database.chatDao().insertOrUpdateChat(updatedChat)

        // 2. Simular respuesta automática realista tras breve delay
        scheduleSimulatedReply(userId, chatId, content)

        return MiraiLinkResult.Success(Unit)
    }

    override fun listenForMessages(callback: (String) -> Unit) {
        // No-op en modo local Room
    }

    private fun scheduleSimulatedReply(userId: String, chatId: String, userMessage: String) {
        scope.launch {
            delay(1800L) // Delay simulado para que se sienta natural

            val feedUser = database.userDao().getFeedUserById(userId)
            val replyText = generateBotReply(feedUser?.nickname ?: "Amigo", userMessage)
            val replyTime = System.currentTimeMillis()

            val botMessage = DemoMessageEntity(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = userId,
                receiverId = DemoDataSeeder.DEMO_USER_ID,
                content = replyText,
                timestamp = replyTime,
                isRead = false,
            )
            database.chatDao().insertMessage(botMessage)

            val chatWithReply = DemoChatEntity(
                id = chatId,
                otherUserId = userId,
                lastMessageText = replyText,
                lastMessageSenderId = userId,
                lastMessageTimestamp = replyTime,
                unreadCount = 1,
            )
            database.chatDao().insertOrUpdateChat(chatWithReply)
        }
    }

    private fun generateBotReply(nickname: String, userMessage: String): String {
        val lower = userMessage.lowercase()
        return when {
            lower.contains("hola") || lower.contains("buenas") || lower.contains("hey") -> {
                "¡Hola! ¿Qué tal tu día? Estaba justo escuchando música de animes ✨"
            }
            lower.contains("anime") || lower.contains("serie") || lower.contains("manga") -> {
                "¡Totalmente de acuerdo! Las recomendaciones de anime siempre vienen bien. ¿Cuál es tu favorito ahora mismo?"
            }
            lower.contains("juego") || lower.contains("game") || lower.contains("jugar") -> {
                "¡Me encanta jugar! Cuando quieras podemos armar una partida en cooperativo 🎮"
            }
            nickname == "Sakura" -> {
                "¡Siii! Me encanta hablar de esto contigo. ¡Tenemos gustos super parecidos! ✨🌸"
            }
            nickname == "Kenji" -> {
                "Jajaja exacto, en los soulslike y en la vida hay que tener paciencia ⚔️"
            }
            nickname == "Aoi" -> {
                "¡Buena esa! A ver si luego nos echamos unas partidas rápidas 🎯"
            }
            else -> {
                "¡Qué interesante! Me gusta mucho hablar contigo por aquí 😊"
            }
        }
    }
}
