package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface MatchRepository {
    suspend fun getMatches(): MiraiLinkResult<List<User>>

    suspend fun getUnseenMatches(): MiraiLinkResult<List<User>>

    suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit>
}