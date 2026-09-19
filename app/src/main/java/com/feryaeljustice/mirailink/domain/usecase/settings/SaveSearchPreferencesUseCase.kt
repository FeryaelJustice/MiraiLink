package com.feryaeljustice.mirailink.domain.usecase.settings

import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SaveSearchPreferencesUseCase(
    private val repository: SearchPreferencesRepository,
) {
    suspend operator fun invoke(preferences: SearchPreferences): MiraiLinkResult<Unit> =
        repository.saveSearchPreferences(preferences)
}
