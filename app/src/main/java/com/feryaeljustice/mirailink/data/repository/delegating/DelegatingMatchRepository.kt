package com.feryaeljustice.mirailink.data.repository.delegating

import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DelegatingMatchRepository(
    private val remoteRepo: MatchRepository,
    private val demoRepo: MatchRepository,
    private val demoModeManager: DemoModeManager,
) : MatchRepository {

    private fun targetRepo(): MatchRepository =
        if (demoModeManager.isDemoActive()) demoRepo else remoteRepo

    override suspend fun getMatches(): MiraiLinkResult<List<User>> = targetRepo().getMatches()

    override suspend fun getUnseenMatches(): MiraiLinkResult<List<User>> =
        targetRepo().getUnseenMatches()

    override suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit> =
        targetRepo().markMatchAsSeen(matchIds)
}
