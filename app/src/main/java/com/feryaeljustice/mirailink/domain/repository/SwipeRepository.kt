package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.model.swipe.UndoSwipeResult
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface SwipeRepository {
    suspend fun getFeed(): MiraiLinkResult<List<User>>
    suspend fun getReceivedLikes(limit: Int = 20, offset: Int = 0): MiraiLinkResult<List<com.feryaeljustice.mirailink.domain.model.swipe.ReceivedLike>>
    suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean>
    suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit>
    suspend fun getUndoQuota(): MiraiLinkResult<UndoQuota>
    suspend fun undoSwipe(targetUserId: String? = null): MiraiLinkResult<UndoSwipeResult>
}