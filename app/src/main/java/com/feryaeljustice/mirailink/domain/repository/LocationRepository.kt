package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface LocationRepository {
    suspend fun sendLocationPing(
        latitude: Double,
        longitude: Double,
        city: String? = null,
        countryCode: String? = null,
    ): MiraiLinkResult<Unit>
}
