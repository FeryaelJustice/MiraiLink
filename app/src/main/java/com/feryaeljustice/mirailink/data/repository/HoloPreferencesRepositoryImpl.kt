package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.repository.HoloPreferencesRepository

class HoloPreferencesRepositoryImpl(private val prefs: MiraiLinkPrefs) : HoloPreferencesRepository {
    override fun observeEnabled() = prefs.getHoloProfileEnabled()
    override suspend fun setEnabled(enabled: Boolean) = prefs.setHoloProfileEnabled(enabled)
}
