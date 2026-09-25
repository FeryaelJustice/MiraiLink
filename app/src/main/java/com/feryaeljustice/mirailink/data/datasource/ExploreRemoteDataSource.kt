package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.explore.CategorySettingsResponseDto
import com.feryaeljustice.mirailink.data.model.explore.ExploreHubResponseDto
import com.feryaeljustice.mirailink.data.model.explore.UpdateCategorySettingsRequestDto
import com.feryaeljustice.mirailink.data.remote.ExploreApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ExploreRemoteDataSource(
    private val api: ExploreApiService,
) {
    suspend fun getCategories(): MiraiLinkResult<ExploreHubResponseDto> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getCategories()
        }

    suspend fun getCategoryFeed(
        categoryId: String,
        limit: Int = 20,
        offset: Int = 0,
    ): MiraiLinkResult<List<UserDto>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getCategoryFeed(categoryId, limit, offset)
        }

    suspend fun getCategorySettings(categoryId: String): MiraiLinkResult<CategorySettingsResponseDto> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getCategorySettings(categoryId)
        }

    suspend fun updateCategorySettings(
        categoryId: String,
        radiusKm: Int,
    ): MiraiLinkResult<CategorySettingsResponseDto> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.updateCategorySettings(categoryId, UpdateCategorySettingsRequestDto(radiusKm))
        }
}
