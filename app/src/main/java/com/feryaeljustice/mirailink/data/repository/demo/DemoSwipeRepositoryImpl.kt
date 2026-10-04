package com.feryaeljustice.mirailink.data.repository.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.toDomainUser
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoSwipeHistoryEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoSwipeUndoEntity
import com.feryaeljustice.mirailink.data.time.TrustedTimeProvider
import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.model.swipe.UndoSwipeResult
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
    private val timeProvider: TrustedTimeProvider = object : TrustedTimeProvider {
        override fun currentTimeMillis(): Long = System.currentTimeMillis()
        override fun isTimeTrusted(): Boolean = true
        override fun syncWithServerTime(serverEpochMillis: Long) {}
    },
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
                    val target = searchPrefs.targetCountryId
                    if (target.isNullOrBlank()) true
                    else user.residenceCountryCode == null || user.residenceCountryCode.equals(target, ignoreCase = true)
                }
            }
        }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }

        return MiraiLinkResult.Success(filteredUsers)
    }

    override suspend fun getReceivedLikes(
        limit: Int,
        offset: Int,
    ): MiraiLinkResult<List<com.feryaeljustice.mirailink.domain.model.swipe.ReceivedLike>> {
        seeder.seedInitialDataIfEmpty()
        val allUsers = database.userDao().getAllFeedUsers()
        val matchedIds = database.matchDao().getAllMatches().map { it.userId }.toSet()
        val receivedLikes = allUsers
            .filter { !it.isLiked && !it.isDisliked && it.id !in matchedIds }
            .drop(offset)
            .take(limit)
            .map { entity ->
                com.feryaeljustice.mirailink.domain.model.swipe.ReceivedLike(
                    likeId = "demo_like_${entity.id}",
                    likedAt = "2026-09-23T12:00:00Z",
                    user = entity.toDomainUser(),
                )
            }
        return MiraiLinkResult.Success(receivedLikes)
    }

    override suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> {
        val now = timeProvider.currentTimeMillis()
        database.userDao().markLiked(toUserId)
        database.userDao().insertSwipeHistory(
            DemoSwipeHistoryEntity(
                targetUserId = toUserId,
                action = "like",
                timestamp = now,
            )
        )
        val feedUser = database.userDao().getFeedUserById(toUserId)

        val isMatch = feedUser?.willMatch ?: true
        if (isMatch) {
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
        val now = timeProvider.currentTimeMillis()
        database.userDao().markDisliked(toUserId)
        database.userDao().insertSwipeHistory(
            DemoSwipeHistoryEntity(
                targetUserId = toUserId,
                action = "dislike",
                timestamp = now,
            )
        )
        return MiraiLinkResult.Success(Unit)
    }

    /**
     * Consulta la cuota diaria de rebobinado en modo Demo offline.
     * Utiliza [timeProvider] (reloj blindado) para calcular los deshaceres registrados
     * en las ultimas 24 horas y verificar si existen entradas en [DemoSwipeHistoryEntity].
     */
    override suspend fun getUndoQuota(): MiraiLinkResult<UndoQuota> {
        val now = timeProvider.currentTimeMillis()
        val since = now - 24 * 60 * 60 * 1000L
        val used = database.userDao().countUndosSince(since)
        val maxUndos = 1
        val remaining = maxOf(0, maxUndos - used)
        val historyCount = database.userDao().countSwipeHistory()
        val hasUndoableSwipe = historyCount > 0
        val canUndo = remaining > 0 && hasUndoableSwipe

        return MiraiLinkResult.Success(
            UndoQuota(
                tier = SubscriptionPlanType.FREE,
                maxUndos = maxUndos,
                usedUndos = used,
                remainingUndos = remaining,
                resetsAt = null,
                hasUndoableSwipe = hasUndoableSwipe,
                canUndo = canUndo,
            )
        )
    }

    /**
     * Revierte el ultimo voto de interaccion en el entorno offline de demostracion.
     *
     * 1. Comprueba el limite diario de 24 horas mediante [timeProvider]. Si se ha superado,
     *    retorna [SubscriptionError.DAILY_UNDO_LIMIT_REACHED].
     * 2. Recupera la entrada mas reciente de [DemoSwipeHistoryEntity] de Room (permitiendo rebobinar
     *    swipes de sesiones previas incluso tras reiniciar la aplicacion).
     * 3. Revierte de forma atomica en SQLite la marca de like/dislike, elimina el match y el chat/mensajes
     *    asociados generados en la demo.
     * 4. Registra el evento en [DemoSwipeUndoEntity] para auditar la cuota consumida.
     * 5. Retorna [UndoSwipeResult] con el usuario original para restaurar la tarjeta visualmente.
     */
    override suspend fun undoSwipe(targetUserId: String?): MiraiLinkResult<UndoSwipeResult> {
        val now = timeProvider.currentTimeMillis()
        val since = now - 24 * 60 * 60 * 1000L
        val used = database.userDao().countUndosSince(since)
        val maxUndos = 1
        if (used >= maxUndos) {
            return MiraiLinkResult.Error(SubscriptionError.DAILY_UNDO_LIMIT_REACHED)
        }

        val historyEntry = if (targetUserId != null) {
            val latest = database.userDao().getLatestSwipeHistory()
            if (latest?.targetUserId == targetUserId) latest else {
                database.userDao().getLatestSwipeHistory()
            }
        } else {
            database.userDao().getLatestSwipeHistory()
        }

        if (historyEntry == null) {
            return MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
        }

        val targetId = historyEntry.targetUserId
        val action = historyEntry.action

        database.userDao().unmarkLikedOrDisliked(targetId)
        if (action == "like") {
            database.matchDao().deleteMatch(targetId)
            val chatId = "chat_$targetId"
            database.chatDao().deleteChat(chatId)
            database.chatDao().deleteMessagesByChatId(chatId)
        }

        database.userDao().deleteSwipeHistory(historyEntry.id)
        database.userDao().insertUndo(DemoSwipeUndoEntity(undoneAt = now))

        val feedUser = database.userDao().getFeedUserById(targetId)
        val restoredUser = feedUser?.toDomainUser() ?: return MiraiLinkResult.Error(ValidationError.INVALID_INPUT)

        val updatedUsed = used + 1
        val remaining = maxOf(0, maxUndos - updatedUsed)
        val remainingHistory = database.userDao().countSwipeHistory()
        val quota = UndoQuota(
            tier = SubscriptionPlanType.FREE,
            maxUndos = maxUndos,
            usedUndos = updatedUsed,
            remainingUndos = remaining,
            resetsAt = null,
            hasUndoableSwipe = remainingHistory > 0,
            canUndo = remaining > 0 && remainingHistory > 0,
        )

        return MiraiLinkResult.Success(
            UndoSwipeResult(
                user = restoredUser,
                actionUndone = action,
                quota = quota,
            )
        )
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
