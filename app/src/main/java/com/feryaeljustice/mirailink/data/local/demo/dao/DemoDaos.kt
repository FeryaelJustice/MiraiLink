package com.feryaeljustice.mirailink.data.local.demo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity

@Dao
interface DemoUserDao {
    @Query("SELECT * FROM demo_user_profile WHERE id = :userId LIMIT 1")
    suspend fun getUserProfile(userId: String = "demo_user_id"): DemoUserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: DemoUserProfileEntity)

    @Update
    suspend fun updateUserProfile(profile: DemoUserProfileEntity)

    @Query("SELECT * FROM demo_feed_users WHERE isLiked = 0 AND isDisliked = 0")
    suspend fun getFeedUsers(): List<DemoFeedUserEntity>

    @Query("SELECT * FROM demo_feed_users WHERE id = :userId LIMIT 1")
    suspend fun getFeedUserById(userId: String): DemoFeedUserEntity?

    @Query("SELECT * FROM demo_feed_users")
    suspend fun getAllFeedUsers(): List<DemoFeedUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedUsers(users: List<DemoFeedUserEntity>)

    @Query("UPDATE demo_feed_users SET isLiked = 1, isDisliked = 0 WHERE id = :userId")
    suspend fun markLiked(userId: String)

    @Query("UPDATE demo_feed_users SET isDisliked = 1, isLiked = 0 WHERE id = :userId")
    suspend fun markDisliked(userId: String)

    @Query("DELETE FROM demo_user_profile")
    suspend fun clearUserProfile()

    @Query("DELETE FROM demo_feed_users")
    suspend fun clearFeedUsers()
}

@Dao
interface DemoMatchDao {
    @Query("SELECT * FROM demo_matches ORDER BY matchedAt DESC")
    suspend fun getAllMatches(): List<DemoMatchEntity>

    @Query("SELECT * FROM demo_matches WHERE isSeen = 0 ORDER BY matchedAt DESC")
    suspend fun getUnseenMatches(): List<DemoMatchEntity>

    @Query("SELECT * FROM demo_matches WHERE userId = :userId LIMIT 1")
    suspend fun getMatchByUserId(userId: String): DemoMatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: DemoMatchEntity)

    @Query("UPDATE demo_matches SET isSeen = 1 WHERE userId IN (:userIds)")
    suspend fun markMatchesAsSeen(userIds: List<String>)

    @Query("DELETE FROM demo_matches")
    suspend fun clearMatches()
}

@Dao
interface DemoChatDao {
    @Query("SELECT * FROM demo_chats ORDER BY lastMessageTimestamp DESC")
    suspend fun getAllChats(): List<DemoChatEntity>

    @Query("SELECT * FROM demo_chats WHERE otherUserId = :otherUserId LIMIT 1")
    suspend fun getChatByUserId(otherUserId: String): DemoChatEntity?

    @Query("SELECT * FROM demo_chats WHERE id = :chatId LIMIT 1")
    suspend fun getChatById(chatId: String): DemoChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateChat(chat: DemoChatEntity)

    @Query("SELECT * FROM demo_messages WHERE (senderId = :userId1 AND receiverId = :userId2) OR (senderId = :userId2 AND receiverId = :userId1) ORDER BY timestamp ASC")
    suspend fun getMessagesBetween(userId1: String, userId2: String): List<DemoMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: DemoMessageEntity)

    @Query("UPDATE demo_chats SET unreadCount = 0 WHERE id = :chatId")
    suspend fun markChatAsRead(chatId: String)

    @Query("DELETE FROM demo_chats")
    suspend fun clearChats()

    @Query("DELETE FROM demo_messages")
    suspend fun clearMessages()
}
