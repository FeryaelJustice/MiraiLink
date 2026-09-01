package com.feryaeljustice.mirailink.data.repository.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.toDomainUser
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import java.util.UUID

class DemoSwipeRepositoryImpl(
    private val database: MiraiLinkDemoDatabase,
    private val seeder: DemoDataSeeder,
) : SwipeRepository {

    override suspend fun getFeed(): MiraiLinkResult<List<User>> {
        seeder.seedInitialDataIfEmpty()
        var feedUsers = database.userDao().getFeedUsers()
        if (feedUsers.isEmpty()) {
            // Si el usuario consumió todo el feed, reiniciamos el estado de like/dislike de los feed users
            val allUsers = database.userDao().getAllFeedUsers()
            if (allUsers.isNotEmpty()) {
                allUsers.forEach { user ->
                    database.userDao().insertFeedUsers(listOf(user.copy(isLiked = false, isDisliked = false)))
                }
                feedUsers = database.userDao().getFeedUsers()
            }
        }
        val users = feedUsers.map { it.toDomainUser() }
        return MiraiLinkResult.Success(users)
    }

    override suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> {
        database.userDao().markLiked(toUserId)
        val feedUser = database.userDao().getFeedUserById(toUserId)

        val isMatch = feedUser?.willMatch ?: true
        if (isMatch) {
            val now = System.currentTimeMillis()
            val match = DemoMatchEntity(
                userId = toUserId,
                matchedAt = now,
                isSeen = false,
            )
            database.matchDao().insertMatch(match)

            val chatId = "chat_$toUserId"
            val initialGreeting = getGreetingForUser(feedUser?.nickname ?: "Usuario")

            val chat = DemoChatEntity(
                id = chatId,
                otherUserId = toUserId,
                lastMessageText = initialGreeting,
                lastMessageSenderId = toUserId,
                lastMessageTimestamp = now,
                unreadCount = 1,
            )
            database.chatDao().insertOrUpdateChat(chat)

            val msg = DemoMessageEntity(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = toUserId,
                receiverId = DemoDataSeeder.DEMO_USER_ID,
                content = initialGreeting,
                timestamp = now,
                isRead = false,
            )
            database.chatDao().insertMessage(msg)
        }

        return MiraiLinkResult.Success(isMatch)
    }

    override suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> {
        database.userDao().markDisliked(toUserId)
        return MiraiLinkResult.Success(Unit)
    }

    private fun getGreetingForUser(nickname: String): String {
        return when (nickname) {
            "Aoi" -> "¡Hey! ¡Qué alegría hacer match contigo! ¿Tienes Discord o juegas en PC?"
            "Kenji" -> "¡Hola! Vi que también te gustan los RPGs. ¿Cuál es tu favorito de todos los tiempos?"
            "Sakura" -> "¡Konnichiwa! ✨ ¡Hicimos match! ¿Qué animes estás viendo esta temporada?"
            "Hiroshi" -> "¡Buenas! Qué alegría coincidir. Me encanta tu perfil 🎮"
            "Yuki" -> "¡Hola! ✨ Me alegra mucho coincidir por aquí. ¿Te gusta el ramen o los mangas de romance?"
            "Ren" -> "¡Hey! Buen match 🤘 ¿Tocas algún instrumento o escuchas rock japonés?"
            else -> "¡Hola! ¡Qué bien que hayamos hecho match! 😊"
        }
    }
}
