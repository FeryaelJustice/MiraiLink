package com.feryaeljustice.mirailink.data.repository.delegating

import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DelegatingSwipeRepository(
    private val remoteRepo: SwipeRepository,
    private val demoRepo: SwipeRepository,
    private val demoModeManager: DemoModeManager,
) : SwipeRepository {

    private fun targetRepo(): SwipeRepository =
        if (demoModeManager.isDemoActive()) demoRepo else remoteRepo

    override suspend fun getFeed(): MiraiLinkResult<List<User>> = targetRepo().getFeed()

    override suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> =
        targetRepo().likeUser(toUserId)

    override suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> =
        targetRepo().dislikeUser(toUserId)
}
