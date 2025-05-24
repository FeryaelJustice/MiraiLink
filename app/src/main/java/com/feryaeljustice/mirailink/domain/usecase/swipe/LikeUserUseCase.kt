package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class LikeUserUseCase @Inject constructor(private val repository: SwipeRepository) {
    suspend operator fun invoke(toUserId: String): MiraiLinkResult<Boolean> = repository.likeUser(toUserId)
}