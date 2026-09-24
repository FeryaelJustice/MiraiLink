package com.feryaeljustice.mirailink.data.local.demo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoCategoryPreferenceEntity

@Dao
interface DemoCategoryDao {
    @Query("SELECT * FROM demo_category_preferences WHERE userId = :userId AND categoryId = :categoryId LIMIT 1")
    suspend fun getPreference(userId: String, categoryId: String): DemoCategoryPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(preference: DemoCategoryPreferenceEntity)

    @Query("DELETE FROM demo_category_preferences WHERE userId = :userId")
    suspend fun clearUserPreferences(userId: String)
}
