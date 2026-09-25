package com.feryaeljustice.mirailink.domain.usecase.explore

import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetCategoryPreferencesUseCase(
    private val repository: ExploreRepository,
) {
    suspend operator fun invoke(categoryId: String): MiraiLinkResult<CategoryPreference> =
        repository.getCategoryPreferences(categoryId)
}
