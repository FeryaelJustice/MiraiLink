package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.Flow

interface SearchPreferencesRepository {
    fun getSearchPreferences(): Flow<SearchPreferences>
    suspend fun saveSearchPreferences(preferences: SearchPreferences): MiraiLinkResult<Unit>
}
