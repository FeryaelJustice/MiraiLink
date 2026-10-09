package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class LikeUserUseCase(
    private val repository: SwipeRepository,
) {
    suspend operator fun invoke(toUserId: String, receivedLikeId: String? = null, discoveryMode: String = "classic"): MiraiLinkResult<Boolean> =
        if (receivedLikeId == null) repository.likeUser(toUserId)
        else repository.returnReceivedLike(toUserId, receivedLikeId, discoveryMode)
}
