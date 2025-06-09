package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface SwipeRepository {
    suspend fun getFeed(): MiraiLinkResult<List<User>>
    suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean>
    suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit>
}