package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.repository.LocationRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class LocationRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val demoModeManager: DemoModeManager,
) : LocationRepository {

    override suspend fun sendLocationPing(
        latitude: Double,
        longitude: Double,
        city: String?,
        countryCode: String?,
    ): MiraiLinkResult<Unit> {
        if (demoModeManager.isDemoMode.value) {
            return MiraiLinkResult.Success(Unit)
        }
        return remoteDataSource.pingLocation(latitude, longitude, city, countryCode)
    }
}
