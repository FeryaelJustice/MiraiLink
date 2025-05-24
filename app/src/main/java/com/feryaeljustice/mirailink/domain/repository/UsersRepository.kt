package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface UsersRepository {
    suspend fun getUsers(): MiraiLinkResult<List<User>>
}