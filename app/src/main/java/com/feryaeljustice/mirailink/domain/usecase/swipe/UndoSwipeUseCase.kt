package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.model.swipe.UndoSwipeResult
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class UndoSwipeUseCase(
    private val repository: SwipeRepository,
) {
    suspend operator fun invoke(targetUserId: String? = null): MiraiLinkResult<UndoSwipeResult> =
        repository.undoSwipe(targetUserId)
}
