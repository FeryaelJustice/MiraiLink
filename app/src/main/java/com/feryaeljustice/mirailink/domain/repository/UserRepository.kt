package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface UserRepository {
    suspend fun testAuth(): MiraiLinkResult<Unit>
    suspend fun login(email: String, username: String, password: String): MiraiLinkResult<String>
    suspend fun logout(): MiraiLinkResult<Boolean>
    suspend fun register(username: String, email: String, password: String): MiraiLinkResult<String>
    suspend fun requestPasswordReset(email: String): MiraiLinkResult<String>
    suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String
    ): MiraiLinkResult<String>

    suspend fun requestVerificationCode(userId: String, type: String): MiraiLinkResult<String>
    suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String
    ): MiraiLinkResult<String>

    suspend fun getCurrentUser(): MiraiLinkResult<User>
    suspend fun getUserById(userId: String): MiraiLinkResult<User>
    suspend fun updateBio(bio: String): MiraiLinkResult<Unit>
}