package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getThemePreference(): Flow<ThemePreference>
    suspend fun setThemePreference(themePreference: ThemePreference)
}
