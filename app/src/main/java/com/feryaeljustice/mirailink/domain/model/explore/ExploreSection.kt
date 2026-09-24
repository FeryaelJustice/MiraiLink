package com.feryaeljustice.mirailink.domain.model.explore

data class ExploreSection(
    val group: ExploreSectionGroup,
    val title: String,
    val categories: List<ExploreCategory>,
)
