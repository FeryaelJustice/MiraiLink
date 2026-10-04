package com.feryaeljustice.mirailink.domain.model.explore

import com.feryaeljustice.mirailink.domain.model.enum.TargetSearchGender

data class CategoryPreference(
    val categoryId: String,
    val radiusKm: Int,
    val targetGender: TargetSearchGender? = null,
)
