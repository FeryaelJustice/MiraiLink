package com.feryaeljustice.mirailink.data.repository.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.toDomainUser
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.GeoUtils
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.error.LocationError
import kotlinx.coroutines.flow.first
import java.util.UUID

class DemoSwipeRepositoryImpl(
    private val database: MiraiLinkDemoDatabase,
    private val seeder: DemoDataSeeder,
    private val searchPreferencesRepository: SearchPreferencesRepository,
) : SwipeRepository {

    override suspend fun getFeed(): MiraiLinkResult<List<User>> {
        seeder.seedInitialDataIfEmpty()
        var feedUsers = database.userDao().getFeedUsers()
        if (feedUsers.isEmpty()) {
            // Si el usuario consumio todo el feed, reiniciamos el estado de like/dislike de los feed users
            val allUsers = database.userDao().getAllFeedUsers()
            if (allUsers.isNotEmpty()) {
                allUsers.forEach { user ->
                    database.userDao().insertFeedUsers(listOf(user.copy(isLiked = false, isDisliked = false)))
                }
                feedUsers = database.userDao().getFeedUsers()
            }
        }

        val searchPrefs = searchPreferencesRepository.getSearchPreferences().first()
        val demoProfile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        val useActiveLocation = searchPrefs.scope == SearchScope.RADIUS_ACTIVE
        val userLat = if (useActiveLocation) demoProfile?.currentLatitude else demoProfile?.residenceLatitude
        val userLon = if (useActiveLocation) demoProfile?.currentLongitude else demoProfile?.residenceLongitude
        if (searchPrefs.scope.isRadiusScope() && (userLat == null || userLon == null)) {
            return MiraiLinkResult.Error(LocationError.LOCATION_REQUIRED)
        }
        if (searchPrefs.scope == SearchScope.MY_COUNTRY && demoProfile?.residenceCountryCode.isNullOrBlank()) {
            return MiraiLinkResult.Error(LocationError.RESIDENCE_COUNTRY_REQUIRED)
        }

        val usersWithDistance = feedUsers.map { entity ->
            val user = entity.toDomainUser()
            val targetLat = if (useActiveLocation) user.currentLatitude else user.residenceLatitude
            val targetLon = if (useActiveLocation) user.currentLongitude else user.residenceLongitude

            val distance = GeoUtils.calculateDistanceKm(userLat, userLon, targetLat, targetLon)
            user.copy(distanceKm = distance)
        }

        val filteredUsers = usersWithDistance.filter { user ->
            when (searchPrefs.scope) {
                SearchScope.RADIUS_RESIDENCE,
                SearchScope.RADIUS_ACTIVE,
                -> {
                    val distance = user.distanceKm
                    distance == null || distance <= searchPrefs.radiusKm
                }
                SearchScope.MY_COUNTRY -> {
                    val myCountry = demoProfile?.residenceCountryCode
                    user.residenceCountryCode == null || user.residenceCountryCode.equals(myCountry, ignoreCase = true)
                }
                SearchScope.WORLD -> true
                SearchScope.SPECIFIC_COUNTRY -> {
                    val target = searchPrefs.targetCountryCode
                    if (target.isNullOrBlank()) true
                    else user.residenceCountryCode == null || user.residenceCountryCode.equals(target, ignoreCase = true)
                }
            }
        }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }

        return MiraiLinkResult.Success(filteredUsers)
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
