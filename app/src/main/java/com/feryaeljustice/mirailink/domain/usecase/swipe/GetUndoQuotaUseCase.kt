package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetUndoQuotaUseCase(
    private val repository: SwipeRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<UndoQuota> = repository.getUndoQuota()
}
