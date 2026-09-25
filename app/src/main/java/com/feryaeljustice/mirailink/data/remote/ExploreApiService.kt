package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.explore.CategorySettingsResponseDto
import com.feryaeljustice.mirailink.data.model.explore.ExploreHubResponseDto
import com.feryaeljustice.mirailink.data.model.explore.UpdateCategorySettingsRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ExploreApiService {
    @GET("explore/categories")
    suspend fun getCategories(): ExploreHubResponseDto

    @GET("explore/categories/{categoryId}/feed")
    suspend fun getCategoryFeed(
        @Path("categoryId") categoryId: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): List<UserDto>

    @GET("explore/categories/{categoryId}/settings")
    suspend fun getCategorySettings(
        @Path("categoryId") categoryId: String,
    ): CategorySettingsResponseDto

    @PUT("explore/categories/{categoryId}/settings")
    suspend fun updateCategorySettings(
        @Path("categoryId") categoryId: String,
        @Body request: UpdateCategorySettingsRequestDto,
    ): CategorySettingsResponseDto
}
