package com.feryaeljustice.mirailink.domain.usecase.explore

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetCategoryFeedUseCase(
    private val repository: ExploreRepository,
) {
    suspend operator fun invoke(
        categoryId: String,
        limit: Int = 20,
        offset: Int = 0,
    ): MiraiLinkResult<List<User>> =
        repository.getCategoryFeed(categoryId, limit, offset)
}
