package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class DislikeUserUseCase @Inject constructor(private val repository: SwipeRepository) {
    suspend operator fun invoke(toUserId: String): MiraiLinkResult<Unit> = repository.dislikeUser(toUserId)
}