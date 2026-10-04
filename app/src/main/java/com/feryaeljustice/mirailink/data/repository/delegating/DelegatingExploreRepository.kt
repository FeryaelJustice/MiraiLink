package com.feryaeljustice.mirailink.data.repository.delegating

import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DelegatingExploreRepository(
    private val remoteRepo: ExploreRepository,
    private val demoRepo: ExploreRepository,
    private val demoModeManager: DemoModeManager,
) : ExploreRepository {

    private fun targetRepo(): ExploreRepository =
        if (demoModeManager.isDemoActive()) demoRepo else remoteRepo

    override suspend fun getExploreHubData(): MiraiLinkResult<ExploreHubData> =
        targetRepo().getExploreHubData()

    override suspend fun getCategoryFeed(
        categoryId: String,
        limit: Int,
        offset: Int,
        radiusKm: Int?,
        targetGender: com.feryaeljustice.mirailink.domain.model.enum.TargetSearchGender?,
    ): MiraiLinkResult<List<User>> =
        targetRepo().getCategoryFeed(categoryId, limit, offset, radiusKm, targetGender)

    override suspend fun getCategoryPreferences(categoryId: String): MiraiLinkResult<CategoryPreference> =
        targetRepo().getCategoryPreferences(categoryId)

    override suspend fun updateCategoryPreferences(
        categoryId: String,
        radiusKm: Int?,
        targetGender: com.feryaeljustice.mirailink.domain.model.enum.TargetSearchGender?,
    ): MiraiLinkResult<CategoryPreference> =
        targetRepo().updateCategoryPreferences(categoryId, radiusKm, targetGender)
}
