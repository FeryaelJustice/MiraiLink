package com.feryaeljustice.mirailink.domain.usecase.explore

import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class UpdateCategoryPreferencesUseCase(
    private val repository: ExploreRepository,
) {
    suspend operator fun invoke(categoryId: String, radiusKm: Int): MiraiLinkResult<CategoryPreference> =
        repository.updateCategoryPreferences(categoryId, radiusKm)
}
