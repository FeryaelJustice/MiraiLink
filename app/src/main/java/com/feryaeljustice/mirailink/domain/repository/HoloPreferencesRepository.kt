package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.Flow

/** Preferencia local del efecto, independiente de red y del modo demo. */
interface HoloPreferencesRepository {
    fun observeEnabled(): Flow<Boolean>
    suspend fun setEnabled(enabled: Boolean): MiraiLinkResult<Unit>
}
