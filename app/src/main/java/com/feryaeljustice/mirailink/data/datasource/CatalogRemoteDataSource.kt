package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.remote.CatalogApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import javax.inject.Inject

class CatalogRemoteDataSource @Inject constructor(private val api: CatalogApiService) {
    suspend fun getAnimes(): MiraiLinkResult<List<AnimeDto>> {
        return try {
            val response = api.getAllAnimes()
            MiraiLinkResult.Success(response)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "CatalogRemoteDataSource", "getAnimes")
        }
    }

    suspend fun getGames(): MiraiLinkResult<List<GameDto>> {
        return try {
            val response = api.getAllGames()
            MiraiLinkResult.Success(response)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "CatalogRemoteDataSource", "getAnimes")
        }
    }
}