package com.feryaeljustice.mirailink.data.local.demo

import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity
import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import kotlinx.serialization.json.Json
import java.util.Date

private val jsonParser = Json { ignoreUnknownKeys = true }

fun DemoUserProfileEntity.toDomainUser(): User {
    val animesList: List<Anime> = runCatching {
        jsonParser.decodeFromString<List<Anime>>(animesJson)
    }.getOrDefault(emptyList())

    val gamesList: List<Game> = runCatching {
        jsonParser.decodeFromString<List<Game>>(gamesJson)
    }.getOrDefault(emptyList())

    val photosList: List<UserPhoto> = runCatching {
        jsonParser.decodeFromString<List<UserPhoto>>(photosJson)
    }.getOrDefault(emptyList())

    return User(
        id = id,
        username = username,
        nickname = nickname,
        email = email,
        phoneNumber = phoneNumber,
        bio = bio,
        gender = gender,
        birthdate = birthdate,
        photos = photosList,
        games = gamesList,
        animes = animesList,
        fcmToken = fcmToken,
    )
}

fun DemoUserProfileEntity.toMinimalUserInfo(): MinimalUserInfo {
    val photosList: List<UserPhoto> = runCatching {
        jsonParser.decodeFromString<List<UserPhoto>>(photosJson)
    }.getOrDefault(emptyList())

    return MinimalUserInfo(
        id = id,
        username = username,
        nickname = nickname,
        email = email,
        gender = gender,
        birthdate = birthdate,
        profilePhoto = photosList.firstOrNull(),
    )
}

fun DemoFeedUserEntity.toDomainUser(): User {
    val animesList: List<Anime> = runCatching {
        jsonParser.decodeFromString<List<Anime>>(animesJson)
    }.getOrDefault(emptyList())

    val gamesList: List<Game> = runCatching {
        jsonParser.decodeFromString<List<Game>>(gamesJson)
    }.getOrDefault(emptyList())

    val photosList: List<UserPhoto> = runCatching {
        jsonParser.decodeFromString<List<UserPhoto>>(photosJson)
    }.getOrDefault(emptyList())

    return User(
        id = id,
        username = username,
        nickname = nickname,
        email = "$username@mirailink.local",
        phoneNumber = null,
        bio = bio,
        gender = gender,
        birthdate = birthdate,
        photos = photosList,
        games = gamesList,
        animes = animesList,
        fcmToken = "",
    )
}

fun DemoFeedUserEntity.toMinimalUserInfo(): MinimalUserInfo {
    val photosList: List<UserPhoto> = runCatching {
        jsonParser.decodeFromString<List<UserPhoto>>(photosJson)
    }.getOrDefault(emptyList())

    return MinimalUserInfo(
        id = id,
        username = username,
        nickname = nickname,
        email = "$username@mirailink.local",
        gender = gender,
        birthdate = birthdate,
        profilePhoto = photosList.firstOrNull(),
    )
}

fun DemoMessageEntity.toDomainChatMessage(
    senderInfo: MinimalUserInfo,
    receiverInfo: MinimalUserInfo,
): ChatMessage {
    return ChatMessage(
        id = id,
        sender = senderInfo,
        receiver = receiverInfo,
        content = content,
        timestamp = timestamp,
    )
}

fun DemoChatEntity.toDomainChatSummary(
    destinataryInfo: MinimalUserInfo?,
): ChatSummary {
    return ChatSummary(
        id = id,
        type = ChatType.PRIVATE,
        createdBy = destinataryInfo?.id ?: "",
        createdAt = Date(lastMessageTimestamp),
        joinedAt = Date(lastMessageTimestamp),
        role = ChatRole.MEMBER,
        lastMessageId = id,
        lastMessageText = lastMessageText,
        lastMessageSenderId = lastMessageSenderId,
        lastMessageSentAt = Date(lastMessageTimestamp),
        unreadCount = unreadCount,
        destinatary = destinataryInfo,
    )
}
