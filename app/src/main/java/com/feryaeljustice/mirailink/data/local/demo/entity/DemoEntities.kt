package com.feryaeljustice.mirailink.data.local.demo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "demo_user_profile")
data class DemoUserProfileEntity(
    @PrimaryKey val id: String = "demo_user_id",
    val username: String,
    val nickname: String,
    val email: String,
    val phoneNumber: String? = null,
    val bio: String,
    val gender: String,
    val birthdate: String,
    val animesJson: String,
    val gamesJson: String,
    val photosJson: String,
    val fcmToken: String? = "",
)

@Entity(tableName = "demo_feed_users")
data class DemoFeedUserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val nickname: String,
    val bio: String,
    val gender: String,
    val birthdate: String,
    val animesJson: String,
    val gamesJson: String,
    val photosJson: String,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val willMatch: Boolean = true,
)

@Entity(tableName = "demo_matches")
data class DemoMatchEntity(
    @PrimaryKey val userId: String,
    val matchedAt: Long,
    val isSeen: Boolean = false,
)

@Entity(tableName = "demo_chats")
data class DemoChatEntity(
    @PrimaryKey val id: String,
    val otherUserId: String,
    val lastMessageText: String,
    val lastMessageSenderId: String?,
    val lastMessageTimestamp: Long,
    val unreadCount: Int = 0,
)

@Entity(tableName = "demo_messages")
data class DemoMessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = true,
)
