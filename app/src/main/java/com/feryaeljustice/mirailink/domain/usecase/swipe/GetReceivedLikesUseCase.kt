package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.model.swipe.ReceivedLike
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetReceivedLikesUseCase(
    private val repository: SwipeRepository,
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
    ): MiraiLinkResult<List<ReceivedLike>> = repository.getReceivedLikes(limit, offset)
}
