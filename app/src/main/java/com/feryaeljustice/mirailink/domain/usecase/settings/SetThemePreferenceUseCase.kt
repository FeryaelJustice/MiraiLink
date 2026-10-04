package com.feryaeljustice.mirailink.domain.usecase.settings

import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.repository.ThemeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SetThemePreferenceUseCase(
    private val repository: ThemeRepository,
) {
    suspend operator fun invoke(themePreference: ThemePreference): MiraiLinkResult<Unit> =
        repository.setThemePreference(themePreference)
}
