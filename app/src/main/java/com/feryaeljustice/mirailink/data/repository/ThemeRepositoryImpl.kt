package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(
    private val miraiLinkPrefs: MiraiLinkPrefs,
) : ThemeRepository {
    override fun getThemePreference(): Flow<ThemePreference> =
        miraiLinkPrefs.getThemePreference()

    override suspend fun setThemePreference(themePreference: ThemePreference) {
        miraiLinkPrefs.setThemePreference(themePreference)
    }
}
