package com.feryaeljustice.mirailink.data.model.explore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExploreCategoryDto(
    @SerialName("id") val id: String,
    @SerialName("code") val code: String,
    @SerialName("sectionGroup") val sectionGroup: String,
    @SerialName("iconKey") val iconKey: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String = "",
    @SerialName("activeCount") val activeCount: Int = 0,
    @SerialName("radiusKm") val radiusKm: Int = 40,
)

@Serializable
data class ExploreSectionDto(
    @SerialName("group") val group: String,
    @SerialName("title") val title: String,
    @SerialName("categories") val categories: List<ExploreCategoryDto>,
)

@Serializable
data class ExploreHubResponseDto(
    @SerialName("recommendations") val recommendations: List<ExploreCategoryDto> = emptyList(),
    @SerialName("sections") val sections: List<ExploreSectionDto> = emptyList(),
)

@Serializable
data class CategorySettingsResponseDto(
    @SerialName("categoryId") val categoryId: String,
    @SerialName("radius_km") val radiusKm: Int,
)

@Serializable
data class UpdateCategorySettingsRequestDto(
    @SerialName("radius_km") val radiusKm: Int,
)
