package com.feryaeljustice.mirailink.domain.usecase.settings

import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class GetThemePreferenceUseCase(
    private val repository: ThemeRepository,
) {
    operator fun invoke(): Flow<ThemePreference> = repository.getThemePreference()
}
