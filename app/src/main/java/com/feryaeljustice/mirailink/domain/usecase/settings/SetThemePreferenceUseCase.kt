package com.feryaeljustice.mirailink.domain.usecase.settings

import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.repository.ThemeRepository

class SetThemePreferenceUseCase(
    private val repository: ThemeRepository,
) {
    suspend operator fun invoke(themePreference: ThemePreference) {
        repository.setThemePreference(themePreference)
    }
}
