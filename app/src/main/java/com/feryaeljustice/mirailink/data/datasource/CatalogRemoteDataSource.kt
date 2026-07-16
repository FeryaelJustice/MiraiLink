package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
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
}
