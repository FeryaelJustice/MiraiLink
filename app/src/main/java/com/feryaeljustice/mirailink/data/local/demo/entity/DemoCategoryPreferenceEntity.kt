package com.feryaeljustice.mirailink.data.local.demo.entity

import androidx.room.Entity

@Entity(
    tableName = "demo_category_preferences",
    primaryKeys = ["userId", "categoryId"],
)
data class DemoCategoryPreferenceEntity(
    val userId: String,
    val categoryId: String,
    val radiusKm: Int,
    val updatedAt: Long = System.currentTimeMillis(),
)
