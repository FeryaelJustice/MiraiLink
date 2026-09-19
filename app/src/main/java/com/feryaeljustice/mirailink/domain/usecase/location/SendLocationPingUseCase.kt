package com.feryaeljustice.mirailink.domain.usecase.location

import com.feryaeljustice.mirailink.domain.repository.LocationRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SendLocationPingUseCase(
    private val repository: LocationRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        city: String? = null,
        countryCode: String? = null,
    ): MiraiLinkResult<Unit> = repository.sendLocationPing(latitude, longitude, city, countryCode)
}
