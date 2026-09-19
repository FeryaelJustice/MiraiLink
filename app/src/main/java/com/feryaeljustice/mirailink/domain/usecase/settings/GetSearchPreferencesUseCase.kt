package com.feryaeljustice.mirailink.domain.usecase.settings

import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSearchPreferencesUseCase(
    private val repository: SearchPreferencesRepository,
) {
    operator fun invoke(): Flow<SearchPreferences> = repository.getSearchPreferences()
}
