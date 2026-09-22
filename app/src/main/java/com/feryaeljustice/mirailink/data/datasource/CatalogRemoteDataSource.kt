package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.model.GeographicPlaceDto
import com.feryaeljustice.mirailink.data.remote.CatalogApiService
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CatalogRemoteDataSource(
    private val api: CatalogApiService,
) {
    suspend fun getAnimes(): MiraiLinkResult<List<AnimeDto>> =
        safeApiCall {
            api.getAllAnimes()
        }

    suspend fun getGames(): MiraiLinkResult<List<GameDto>> =
        safeApiCall {
            api.getAllGames()
        }

    suspend fun getCountries(): MiraiLinkResult<List<GeographicPlaceDto>> =
        safeApiCall { api.getCountries() }

    suspend fun getRegions(countryId: String): MiraiLinkResult<List<GeographicPlaceDto>> =
        safeApiCall { api.getRegions(countryId) }

    suspend fun getCities(regionId: String, query: String): MiraiLinkResult<List<GeographicPlaceDto>> =
        safeApiCall { api.getCities(regionId, query) }
}
