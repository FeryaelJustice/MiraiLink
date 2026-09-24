package com.feryaeljustice.mirailink.domain.model.explore

data class ExploreCategory(
    val id: String,
    val code: String,
    val sectionGroup: ExploreSectionGroup,
    val iconKey: String,
    val title: String,
    val description: String,
    val activeCount: Int,
    val radiusKm: Int,
)
