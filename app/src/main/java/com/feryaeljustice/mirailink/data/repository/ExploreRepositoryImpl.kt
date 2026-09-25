package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ExploreRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.model.explore.ExploreCategoryDto
import com.feryaeljustice.mirailink.data.model.explore.ExploreSectionDto
import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSection
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls

class ExploreRepositoryImpl(
    private val remote: ExploreRemoteDataSource,
    private val baseUrl: String,
) : ExploreRepository {

    override suspend fun getExploreHubData(): MiraiLinkResult<ExploreHubData> =
        when (val result = remote.getCategories()) {
            is MiraiLinkResult.Success -> {
                val data = ExploreHubData(
                    recommendations = result.data.recommendations.map { it.toDomain() },
                    sections = result.data.sections.map { it.toDomain() },
                )
                MiraiLinkResult.Success(data)
            }
            is MiraiLinkResult.Error -> result
        }

    override suspend fun getCategoryFeed(
        categoryId: String,
        limit: Int,
        offset: Int,
    ): MiraiLinkResult<List<User>> =
        when (val result = remote.getCategoryFeed(categoryId, limit, offset)) {
            is MiraiLinkResult.Success -> {
                val users = result.data.map { userDto ->
                    val user = userDto.toDomain()
                    val orderedPhotos = resolvePhotoUrls(baseUrl, user.photos)
                    user.copy(photos = orderedPhotos)
                }
                MiraiLinkResult.Success(users)
            }
            is MiraiLinkResult.Error -> result
        }

    override suspend fun getCategoryPreferences(categoryId: String): MiraiLinkResult<CategoryPreference> =
        when (val result = remote.getCategorySettings(categoryId)) {
            is MiraiLinkResult.Success -> {
                MiraiLinkResult.Success(
                    CategoryPreference(
                        categoryId = result.data.categoryId,
                        radiusKm = result.data.radiusKm,
                    )
                )
            }
            is MiraiLinkResult.Error -> result
        }

    override suspend fun updateCategoryPreferences(
        categoryId: String,
        radiusKm: Int,
    ): MiraiLinkResult<CategoryPreference> =
        when (val result = remote.updateCategorySettings(categoryId, radiusKm)) {
            is MiraiLinkResult.Success -> {
                MiraiLinkResult.Success(
                    CategoryPreference(
                        categoryId = result.data.categoryId,
                        radiusKm = result.data.radiusKm,
                    )
                )
            }
            is MiraiLinkResult.Error -> result
        }
}

private fun ExploreCategoryDto.toDomain(): ExploreCategory =
    ExploreCategory(
        id = id,
        code = code,
        sectionGroup = ExploreSectionGroup.fromRaw(sectionGroup),
        iconKey = iconKey,
        title = title,
        description = description,
        activeCount = activeCount,
        radiusKm = radiusKm,
    )

private fun ExploreSectionDto.toDomain(): ExploreSection =
    ExploreSection(
        group = ExploreSectionGroup.fromRaw(group),
        title = title,
        categories = categories.map { it.toDomain() },
    )
