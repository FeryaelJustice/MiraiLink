package com.feryaeljustice.mirailink.data.repository.demo

import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.toDomainUser
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DemoMatchRepositoryImpl(
    private val database: MiraiLinkDemoDatabase,
) : MatchRepository {

    override suspend fun getMatches(): MiraiLinkResult<List<User>> {
        val matches = database.matchDao().getAllMatches()
        val users = matches.mapNotNull { match ->
            database.userDao().getFeedUserById(match.userId)?.toDomainUser()
        }
        return MiraiLinkResult.Success(users)
    }

    override suspend fun getUnseenMatches(): MiraiLinkResult<List<User>> {
        val unseenMatches = database.matchDao().getUnseenMatches()
        val users = unseenMatches.mapNotNull { match ->
            database.userDao().getFeedUserById(match.userId)?.toDomainUser()
        }
        return MiraiLinkResult.Success(users)
    }

    override suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit> {
        database.matchDao().markMatchesAsSeen(matchIds)
        return MiraiLinkResult.Success(Unit)
    }
}
