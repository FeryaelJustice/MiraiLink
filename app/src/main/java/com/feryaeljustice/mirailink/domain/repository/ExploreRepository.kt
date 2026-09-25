package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSection
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

data class ExploreHubData(
    val recommendations: List<ExploreCategory>,
    val sections: List<ExploreSection>,
)

interface ExploreRepository {
    suspend fun getExploreHubData(): MiraiLinkResult<ExploreHubData>
    suspend fun getCategoryFeed(categoryId: String, limit: Int = 20, offset: Int = 0): MiraiLinkResult<List<User>>
    suspend fun getCategoryPreferences(categoryId: String): MiraiLinkResult<CategoryPreference>
    suspend fun updateCategoryPreferences(categoryId: String, radiusKm: Int): MiraiLinkResult<CategoryPreference>
}
